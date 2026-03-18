package org.foodie_tour.modules.report.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.entity.Booking;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.booking.repository.BookingRepository;
import org.foodie_tour.modules.report.dto.BookingReportResponse;
import org.foodie_tour.modules.report.dto.ReportResponse;
import org.foodie_tour.modules.report.dto.TransactionReportResponse;
import org.foodie_tour.modules.report.mapper.ReportMapper;
import org.foodie_tour.modules.report.service.ReportService;
import org.foodie_tour.modules.transaction.entity.Transactions;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.foodie_tour.modules.transaction.repository.TransactionsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {
    BookingRepository bookingRepository;
    TransactionsRepository transactionsRepository;
    ReportMapper reportMapper;

    private record DateRange(LocalDateTime from, LocalDateTime to) {
    }

    private DateRange resolveDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null && to == null) {
            LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
            return new DateRange(todayStart.minusDays(29), todayStart.plusDays(1));
        }

        if (from == null) {
            LocalDateTime resolvedTo = to.toLocalDate().atStartOfDay().plusDays(1);
            return new DateRange(resolvedTo.minusDays(30), resolvedTo);
        }

        if (to == null) {
            LocalDateTime resolvedFrom = from.toLocalDate().atStartOfDay();
            return new DateRange(resolvedFrom, resolvedFrom.plusDays(30));
        }

        return new DateRange(from, to);
    }

    @Transactional(readOnly = true)
    public ReportResponse generateReport(LocalDateTime from, LocalDateTime to) {
        DateRange dateRange = resolveDateRange(from, to);
        LocalDateTime resolvedFrom = dateRange.from();
        LocalDateTime resolvedTo = dateRange.to();

        long cashIncome = transactionsRepository.totalAmountBetweenAndType(resolvedFrom, resolvedTo, CashFlow.INCOME);
        long cashOutcome = transactionsRepository.totalAmountBetweenAndType(resolvedFrom, resolvedTo, CashFlow.OUTCOME);

        long totalBookingCompleted = bookingRepository.findTotalBetweenAndStatus(resolvedFrom, resolvedTo, BookingStatus.COMPLETED);
        long totalBookingCancelled = bookingRepository.findTotalBetweenAndStatus(resolvedFrom, resolvedTo, BookingStatus.CANCELLED);

        long totalRevenue = bookingRepository.countTotalRevenueBetween(resolvedFrom, resolvedTo, BookingStatus.COMPLETED);

        long totalCustomer = bookingRepository.countTotalCustomerBetween(resolvedFrom, resolvedTo, BookingStatus.COMPLETED);

        List<Transactions> incomeListInPeriod = transactionsRepository.getLastedTransactionsBetweenByType(
                resolvedFrom,
                resolvedTo,
                CashFlow.INCOME,
                PageRequest.of(0, 5)
        );
        List<Transactions> outcomeListInPeriod = transactionsRepository.getLastedTransactionsBetweenByType(
                resolvedFrom,
                resolvedTo,
                CashFlow.OUTCOME,
                PageRequest.of(0, 5)
        );

        List<Booking> bookingList = bookingRepository.findAllBetweenAndStatus(resolvedFrom, resolvedTo, BookingStatus.COMPLETED, PageRequest.of(0, 5));

        return ReportResponse.builder()
                .bookingCompleted(totalBookingCompleted)
                .bookingCancelled(totalBookingCancelled)
                .totalRevenue(totalRevenue)
                .totalCustomer(totalCustomer)
                .income(cashIncome)
                .outcome(cashOutcome)
                .incomeList(incomeListInPeriod.stream().map(reportMapper::toTransactionReportResponse).toList())
                .outcomeList(outcomeListInPeriod.stream().map(reportMapper::toTransactionReportResponse).toList())
                .bookingCompletedList(bookingList.stream().map(reportMapper::toBookingReportResponse).toList())
                .build();
    }

    @Transactional(readOnly = true)
    public List<BookingReportResponse> getBookingReport(LocalDateTime from, LocalDateTime to, Integer page, BookingStatus status) {
        DateRange dateRange = resolveDateRange(from, to);
        LocalDateTime resolvedFrom = dateRange.from();
        LocalDateTime resolvedTo = dateRange.to();

        if (page == null) {
            page = 0;
        }

        List<Booking> bookingList = bookingRepository.findAllBetweenAndStatus(resolvedFrom, resolvedTo, status, PageRequest.of(page, 10));

        return bookingList.stream().map(reportMapper::toBookingReportResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionReportResponse> getTransactionReport(LocalDateTime from, LocalDateTime to, Integer page, CashFlow cashFlow) {
        DateRange dateRange = resolveDateRange(from, to);
        LocalDateTime resolvedFrom = dateRange.from();
        LocalDateTime resolvedTo = dateRange.to();

        if (page == null) {
            page = 0;
        }

        List<Transactions> transactionsList = transactionsRepository.getLastedTransactionsBetweenByType(resolvedFrom, resolvedTo, cashFlow, PageRequest.of(page, 10));

        return transactionsList.stream().map(reportMapper::toTransactionReportResponse).toList();
    }


}

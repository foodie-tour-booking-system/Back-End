package org.foodie_tour.modules.report.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.booking.enums.BookingStatus;
import org.foodie_tour.modules.report.dto.BookingReportResponse;
import org.foodie_tour.modules.report.dto.ReportResponse;
import org.foodie_tour.modules.report.dto.TransactionReportResponse;
import org.foodie_tour.modules.report.service.ReportService;
import org.foodie_tour.modules.transaction.enums.CashFlow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/report")
public class ReportController {
    ReportService reportService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_REPORT')")
    public ResponseEntity<ReportResponse> generateReport(@RequestParam (value = "from", required = false) LocalDateTime from,
                                                         @RequestParam (value = "to", required = false) LocalDateTime to) {
        var result = reportService.generateReport(from, to);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasAuthority('VIEW_REPORT')")
    public ResponseEntity<List<BookingReportResponse>> getBookingReport(
            @RequestParam(value = "from", required = false) LocalDateTime from,
            @RequestParam(value = "to", required = false) LocalDateTime to,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "status") BookingStatus status
    ) {
        var result = reportService.getBookingReport(from, to, page, status);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('VIEW_REPORT')")
    public ResponseEntity<List<TransactionReportResponse>> getTransactionReport(
            @RequestParam(value = "from", required = false) LocalDateTime from,
            @RequestParam(value = "to", required = false) LocalDateTime to,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "cashFlow") CashFlow cashFlow
    ) {
        var result = reportService.getTransactionReport(from, to, page, cashFlow);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

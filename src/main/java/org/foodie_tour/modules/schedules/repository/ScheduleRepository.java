package org.foodie_tour.modules.schedules.repository;

import org.foodie_tour.modules.schedules.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    @Query(value = "SELECT s.departureAt FROM Schedule s WHERE s.scheduleId = :scheduleId")
    Optional<LocalDateTime> getDepartureTime(@Param(value = "scheduleId") long scheduleId);
}

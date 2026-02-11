package org.foodie_tour.modules.schedules.repository;

import org.foodie_tour.modules.schedules.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

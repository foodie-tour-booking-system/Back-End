package org.foodie_tour.modules.schedules.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.schedules.dto.request.ScheduleRequest;
import org.foodie_tour.modules.schedules.dto.response.ScheduleResponse;
import org.foodie_tour.modules.schedules.entity.Schedule;
import org.foodie_tour.modules.schedules.enums.ScheduleStatus;
import org.foodie_tour.modules.schedules.mapper.ScheduleMapper;
import org.foodie_tour.modules.schedules.repository.ScheduleRepository;
import org.foodie_tour.modules.schedules.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @PostMapping("")
    @PreAuthorize("hasAuthority('CREATE_SCHEDULE')")
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody ScheduleRequest request) {
        ScheduleResponse response = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @RequestParam(required = false) Long tourId,
            @RequestParam(required = false) Long routeId,
            @RequestParam(required = false) ScheduleStatus scheduleStatus) {
        List<ScheduleResponse> responses = scheduleService.getSchedules(tourId, routeId, scheduleStatus);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_SCHEDULE')")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleRequest request
    ) {
        ScheduleResponse response = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_SCHEDULE')")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Xóa lịch trình thành công");
    }

    @PostMapping("/template")
    public ResponseEntity<String> createTemplate(@RequestBody ScheduleRequest request) {
        Schedule template = scheduleMapper.toEntity(request);
        template.setIsTemplate(true);
        template.setScheduleStatus(ScheduleStatus.ACTIVE);
        scheduleRepository.save(template);
        return ResponseEntity.ok("Tạo khung giờ mẫu thành công");
    }
}

package org.foodie_tour.modules.system.controller;

import lombok.RequiredArgsConstructor;
import org.foodie_tour.modules.system.entity.SystemConfig;
import org.foodie_tour.modules.system.repository.SystemConfigRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SystemConfigRepository systemConfigRepository;

    @PutMapping("/cancel-hours")
    public ResponseEntity<String> updateCancelHours(@RequestParam String hours) {
        SystemConfig config = systemConfigRepository.findById("CANCEL_ALLOW_HOURS")
                .orElse(new SystemConfig("CANCEL_ALLOW_HOURS", hours, "Thời gian hủy tour được hoàn tiền"));
        config.setConfigValue(hours);
        systemConfigRepository.save(config);
        return ResponseEntity.ok("Cập nhật thời gian hủy tour thành công: " + hours + " giờ.");
    }
}
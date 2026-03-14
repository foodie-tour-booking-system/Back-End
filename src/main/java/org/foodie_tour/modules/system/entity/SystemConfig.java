package org.foodie_tour.modules.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "system_configs")
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig {

    @Id
    private String configKey;
    private String configValue;
    private String description;
}
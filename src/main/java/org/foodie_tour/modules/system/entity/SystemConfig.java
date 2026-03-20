package org.foodie_tour.modules.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "system_configs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SystemConfig {

    @Id
    private String configKey;
    private String configValue;
    private String description;
}
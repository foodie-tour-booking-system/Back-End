package org.foodie_tour.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.rag.RagDataInit;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RagDataConfig {

    RagDataInit ragDataInit;

    @Bean
    @Transactional
    ApplicationRunner initRagData() {
        return args -> ragDataInit.initData();
    }

}

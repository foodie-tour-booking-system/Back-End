package org.foodie_tour.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DotEnvConfig {

    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        dotenv
                .entries()
                .forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}

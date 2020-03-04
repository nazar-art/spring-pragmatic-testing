package org.pgramatictesting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("pokemon")
public class PokemonProperties {
    private String home;
}

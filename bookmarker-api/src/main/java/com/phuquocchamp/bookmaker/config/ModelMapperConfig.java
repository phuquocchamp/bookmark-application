package com.phuquocchamp.bookmaker.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        // Config Matching Strategy
        // modelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return new ModelMapper();
    }
}

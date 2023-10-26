package com.ultimatestorytelling.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.ultimatestorytelling.backend")
public class BeanConfiguration {

    public BeanConfiguration() {}

    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }

}

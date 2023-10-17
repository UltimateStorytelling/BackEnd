package com.ultimatestorytelling.backend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.ultimatestorytelling.backend")
@EnableJpaRepositories(basePackages = "com.ultimatestorytelling.backend")
public class JPAConfiguration {

}

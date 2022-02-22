package com.epam.esm.model.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//dev.properties - developer configuration, prod.properties - for production
@PropertySource(value = {"classpath:props/db.properties",
        "classpath:props/dev.properties"})
@EntityScan(basePackages = "com.epam.esm.model.dto")
public class DaoConfig {
}

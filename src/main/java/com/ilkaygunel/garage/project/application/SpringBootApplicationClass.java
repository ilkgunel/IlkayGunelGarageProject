package com.ilkaygunel.garage.project.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ilkaygunel.garage.project.*"})
public class SpringBootApplicationClass {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationClass.class);
    }

}

package com.consensys.demo.web;

import com.consensys.demo.common.validation.PasswordValidator;
import com.consensys.demo.common.validation.UsernameValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@ComponentScan("com.consensys.demo")
@PropertySource("classpath:/common.properties")
public class WebApplication {

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator();
    }

    @Bean
    public UsernameValidator usernameValidator() {
        return new UsernameValidator();
    }

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}

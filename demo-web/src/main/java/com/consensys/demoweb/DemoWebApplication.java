package com.consensys.demoweb;

import com.consensys.demoweb.validation.PasswordValidator;
import com.consensys.demoweb.validation.UsernameValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class DemoWebApplication {

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator();
    }

    @Bean
    public UsernameValidator usernameValidator() {
        return new UsernameValidator();
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoWebApplication.class, args);
	}
}

package com.consensys.demo.pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.consensys.demo")
@PropertySource("classpath:/common.properties")
public class PdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfApplication.class, args);
	}
}

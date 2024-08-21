package edu.example.restz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.example.restz", "edu.example.sample"})

public class RestzApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestzApplication.class, args);
	}

}

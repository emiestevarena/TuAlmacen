package com.egg.TuAlmacen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TuAlmacenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TuAlmacenApplication.class, args);
	}

}

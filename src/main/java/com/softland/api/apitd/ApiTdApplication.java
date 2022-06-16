package com.softland.api.apitd;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.softland")
@SpringBootApplication
//@EnableEncryptableProperties
public class ApiTdApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ApiTdApplication.class, args);
	}


}

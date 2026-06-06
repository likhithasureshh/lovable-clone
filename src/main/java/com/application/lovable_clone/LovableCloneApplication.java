package com.application.lovable_clone;

import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.AuthService;
import com.application.lovable_clone.service.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LovableCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(LovableCloneApplication.class, args);
	}

}

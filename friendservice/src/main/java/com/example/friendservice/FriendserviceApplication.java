package com.example.friendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FriendserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendserviceApplication.class, args);
	}

}

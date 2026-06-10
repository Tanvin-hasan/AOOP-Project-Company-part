package com.habu.job_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

public class JobSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(JobSystemApplication.class, args);
		new Thread(() -> {
			try {
				com.habu.job_system.socket.SocketServer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

}
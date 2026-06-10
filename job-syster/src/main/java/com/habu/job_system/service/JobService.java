package com.habu.job_system.service;

import com.habu.job_system.entity.Job;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    public void processJob(Job job) {

        new Thread(() -> {
            try {

                System.out.println("Processing job: " + job.getTitle());

                Thread.sleep(2000);

                job.setStatus("ACTIVE");

                System.out.println("Done: " + job.getTitle());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
package com.habu.job_system.repository;

import com.habu.job_system.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByUserEmail(String email);

    List<Application> findByCompanyName(String companyName);
}
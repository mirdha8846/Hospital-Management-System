package com.example.hms.repo;

import com.example.hms.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Doctorrepo extends JpaRepository<Doctor,Long> {
}

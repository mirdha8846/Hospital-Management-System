package com.example.hms.repo;

import com.example.hms.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Appointmentrepo extends JpaRepository<Appointment,Long> {
}

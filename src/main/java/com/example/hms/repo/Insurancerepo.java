package com.example.hms.repo;

import com.example.hms.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Insurancerepo extends JpaRepository<Insurance,Long> {
}

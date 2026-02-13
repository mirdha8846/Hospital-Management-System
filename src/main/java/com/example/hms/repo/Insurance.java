package com.example.hms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Insurance  extends JpaRepository<Insurance,Long> {
}

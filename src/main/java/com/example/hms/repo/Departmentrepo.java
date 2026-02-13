package com.example.hms.repo;

import com.example.hms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Departmentrepo extends JpaRepository<Department,Long> {
}

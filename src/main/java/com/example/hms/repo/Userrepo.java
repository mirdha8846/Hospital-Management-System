package com.example.hms.repo;

import com.example.hms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userrepo extends JpaRepository<User,Long> {
}

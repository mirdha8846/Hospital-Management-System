package com.example.hms.repo;

import com.example.hms.entity.User;
import com.example.hms.entity.type.Authprovidertype;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface Userrepo extends JpaRepository<User,Long> {

    Optional<User> findByUsernam(String username);

    Optional<User> findByProviderIdAndProviderType(String providerId, Authprovidertype providerType);
}

package com.example.hms.security;

import com.example.hms.repo.Userrepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//@NoArgsConstructor
@RequiredArgsConstructor
public class CustomerDetail implements UserDetailsService {
    private final Userrepo userrepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userrepo.findByUsernam(username).orElseThrow();
    }

}

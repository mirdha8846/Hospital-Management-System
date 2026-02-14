package com.example.hms.controller;

import com.example.hms.dto.LoginRequestDto;
import com.example.hms.dto.LoginResponseDto;
import com.example.hms.dto.SignUpRequestDto;
import com.example.hms.dto.SignupResponseDto;
import com.example.hms.security.Authservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Authcontroller {
    private final Authservice authservice;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authservice.login(loginRequestDto));
    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authservice.signup(signupRequestDto));
    }
}

package com.example.hms.controller;


import com.example.hms.dto.AppointmentResponseDto;
import com.example.hms.entity.User;
import com.example.hms.service.Appointmentservice;
import com.example.hms.service.Doctorservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class Doctorcontroller {
    private final Appointmentservice appointmentservice;
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(appointmentservice.getAllAppointmentsOfDoctor(user.getId()));
    }
}

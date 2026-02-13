package com.example.hms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CreateAppointRequestDto {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime appointmentTime;
    private String reason;
}

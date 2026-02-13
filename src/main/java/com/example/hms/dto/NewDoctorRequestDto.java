package com.example.hms.dto;

import lombok.Data;

@Data
public class NewDoctorRequestDto {
    private Long userId;
    private String specialization;
    private String name;
}

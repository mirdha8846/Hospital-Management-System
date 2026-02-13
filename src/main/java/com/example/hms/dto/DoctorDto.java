package com.example.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDto {
    private Long id;
    private String name;
    private String specialization;
    private String email;

}

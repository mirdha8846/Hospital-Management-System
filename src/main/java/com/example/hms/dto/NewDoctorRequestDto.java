package com.example.hms.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewDoctorRequestDto {
    private Long userId;
    private String specialization;
    private String name;
}

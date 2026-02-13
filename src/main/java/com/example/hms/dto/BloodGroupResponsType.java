package com.example.hms.dto;

import com.example.hms.entity.type.BloodGroupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodGroupResponsType {

    private BloodGroupType bloodGroupType;
    private long count;

}

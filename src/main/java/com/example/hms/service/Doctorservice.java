package com.example.hms.service;

import com.example.hms.dto.DoctorDto;
import com.example.hms.dto.NewDoctorRequestDto;
import com.example.hms.entity.Doctor;
import com.example.hms.entity.User;
import com.example.hms.entity.type.RoleType;
import com.example.hms.repo.Doctorrepo;
import com.example.hms.repo.Userrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Doctorservice {
private final Doctorrepo doctorrepo;
private final Userrepo userrepo;
private final ModelMapper modelMapper;

public List<DoctorDto> getAllDoctors(){
    return doctorrepo.findAll().stream().map((element) -> modelMapper.map(element, DoctorDto.class)).collect(Collectors.toList());
}



@Transactional
    public NewDoctorRequestDto onBoardNewDoctor(NewDoctorRequestDto newdoctor){

    User user = userrepo.findById(newdoctor.getUserId()).orElseThrow();

    if(doctorrepo.existsById(newdoctor.getUserId())) {
        throw new IllegalArgumentException("Already a doctor");
    }

    Doctor doctor = Doctor.builder()
            .name(newdoctor.getName())
            .specialization(newdoctor.getSpecialization())
            .user(user)
            .build();


    user.getRoles().add(RoleType.DOCTOR);

    return modelMapper.map(doctorrepo.save(doctor), NewDoctorRequestDto.class);
}
}

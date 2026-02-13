package com.example.hms.service;

import com.example.hms.dto.DoctorDto;
import com.example.hms.dto.NewDoctorRequestDto;
import com.example.hms.entity.Doctor;
import com.example.hms.entity.User;
import com.example.hms.repo.Doctorrepo;
import com.example.hms.repo.Userrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Doctorservice {
private final Doctorrepo doctorrepo;
private final Userrepo userrepo;
private final ModelMapper modelMapper;

public List<Doctor> getAllDoctors(){
    return doctorrepo.findAll();
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

    //this we do later
//    user.getRoles().add(RoleType.DOCTOR);

    return modelMapper.map(doctorrepo.save(doctor), NewDoctorRequestDto.class);
}
}

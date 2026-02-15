package com.example.hms.service;

import com.example.hms.config.Appconfig;
import com.example.hms.dto.PatientDto;
import com.example.hms.dto.PatientResponseDto;
import com.example.hms.entity.Doctor;
import com.example.hms.entity.Patient;
import com.example.hms.repo.Patientrepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Patientservice {
//    private final Patient patient;
//    private final Doctor doctor;
    private final Patientrepo patientrepo;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientResponseDto getPatientById(Long id){
        Patient patient1=patientrepo.findById(id).orElseThrow(()->new EntityNotFoundException("thhis is not out patient"));
        return modelMapper.map(patient1,PatientResponseDto.class);
    }
    @Transactional
    public List<PatientResponseDto> getAllPatients(Integer pagenumber, Integer pagesize){
        return patientrepo.findAllPatitent(PageRequest.of(pagenumber,pagesize))
                .stream()
                .map((p)->modelMapper.map(p,PatientResponseDto.class)).collect(Collectors.toList());


    }


}

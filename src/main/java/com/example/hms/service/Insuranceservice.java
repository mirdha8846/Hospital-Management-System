package com.example.hms.service;

import com.example.hms.dto.PatientDto;
import com.example.hms.entity.Insurance;
import com.example.hms.entity.Patient;
import com.example.hms.repo.Insurancerepo;
import com.example.hms.repo.Patientrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Insuranceservice {
private final Insurancerepo insurancerepo;
private final Patientrepo patientrepo;
private final ModelMapper modelMapper;

@Transactional
    public PatientDto assignInsuranceToPatient(Insurance insurance,Long patientId){
    Patient patient= patientrepo.findById(patientId).orElseThrow();
    patient.setInsurance(insurance);
    return modelMapper.map(patient,PatientDto.class);
}

    @Transactional
    public PatientDto disaccociateInsuranceFromPatient(Long patientId) {
        Patient patient= patientrepo.findById(patientId).orElseThrow();
        patient.setInsurance(null);
        return modelMapper.map(patient,PatientDto.class);
    }
}

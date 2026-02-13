package com.example.hms.service;

import com.example.hms.entity.Insurance;
import com.example.hms.entity.Patient;
import com.example.hms.repo.Insurancerepo;
import com.example.hms.repo.Patientrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Insuranceservice {
private final Insurancerepo insurancerepo;
private final Patientrepo patientrepo;

@Transactional
    public Patient assignInsuranceToPatient(Insurance insurance,Long patientId){
    Patient patient= patientrepo.findById(patientId).orElseThrow();
    patient.setInsurance(insurance);
    return patient;
}

    @Transactional
    public Patient disaccociateInsuranceFromPatient(Long patientId) {
        Patient patient= patientrepo.findById(patientId).orElseThrow();
        patient.setInsurance(null);
        return patient;
    }
}

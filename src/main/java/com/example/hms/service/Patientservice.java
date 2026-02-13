package com.example.hms.service;

import com.example.hms.entity.Doctor;
import com.example.hms.entity.Patient;
import com.example.hms.repo.Patientrepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Patientservice {
    private final Patient patient;
    private final Doctor doctor;
    private final Patientrepo patientrepo;

    @Transactional
    public Patient getPatientById(Long id){
        Patient patient1=patientrepo.findById(id).orElseThrow(()->new EntityNotFoundException("thhis is not out patient"));
        return patient1;
    }


}

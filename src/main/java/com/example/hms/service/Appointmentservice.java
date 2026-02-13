package com.example.hms.service;

import com.example.hms.entity.Appointment;
import com.example.hms.repo.Appointmentrepo;
import com.example.hms.repo.Doctorrepo;
import com.example.hms.repo.Patientrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Appointmentservice {
    private final Appointmentrepo appointmentrepo;
    private final Patientrepo patientrepo;
    private  final Doctorrepo doctorrepo;

//    @Transactional
//    public Appointment createNewAppointment(){
//
//    }

}

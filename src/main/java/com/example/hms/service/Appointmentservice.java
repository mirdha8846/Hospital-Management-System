package com.example.hms.service;

import com.example.hms.dto.AppointmentResponseDto;
import com.example.hms.dto.CreateAppointRequestDto;
import com.example.hms.entity.Appointment;
import com.example.hms.entity.Doctor;
import com.example.hms.entity.Patient;
import com.example.hms.repo.Appointmentrepo;
import com.example.hms.repo.Doctorrepo;
import com.example.hms.repo.Patientrepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class Appointmentservice {
    private final Appointmentrepo appointmentrepo;
    private final Patientrepo patientrepo;
    private  final Doctorrepo doctorrepo;
    private final ModelMapper modelMapper;

    @Transactional
    public AppointmentResponseDto createNewAppointment(CreateAppointRequestDto createAppointRequestDto){
        Long doctorId = createAppointRequestDto.getDoctorId();
        Long patientId = createAppointRequestDto.getPatientId();

        Patient patient = patientrepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
        Doctor doctor = doctorrepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        Appointment appointment= Appointment.builder().reason(createAppointRequestDto.getReason())
                .appointmentTime(createAppointRequestDto.getAppointmentTime())
                .build();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        patient.getAppointments().add(appointment); // to maintain consistency

        appointment = appointmentrepo.save(appointment);
        return modelMapper.map(appointment, AppointmentResponseDto.class);

    }


    @Transactional

    public Appointment reAssignAppointmentToAnotherDoctor(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentrepo.findById(appointmentId).orElseThrow();
        Doctor doctor = doctorrepo.findById(doctorId).orElseThrow();

        appointment.setDoctor(doctor); // this will automatically call the update, because it is dirty


        return appointment;
    }


    public List<AppointmentResponseDto> getAllAppointmentsOfDoctor(Long doctorId) {
        Doctor doctor = doctorrepo.findById(doctorId).orElseThrow();

        return doctor.getAppointments()
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }

}

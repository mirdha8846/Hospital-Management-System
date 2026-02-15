package com.example.hms.controller;


import com.example.hms.dto.DoctorDto;
import com.example.hms.dto.NewDoctorRequestDto;
import com.example.hms.dto.PatientResponseDto;
import com.example.hms.service.Doctorservice;
import com.example.hms.service.Patientservice;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Admincontroller {
    private final Patientservice patientservice;
    private final Doctorservice doctorservice;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPaitnets(
            @RequestParam(value = "page",defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size",defaultValue = "10") Integer pageSize
    ){
        return ResponseEntity.ok(patientservice.getAllPatients(pageNumber,pageSize));
    }

    @PostMapping("/onBoardNewDoctor")
    public ResponseEntity<NewDoctorRequestDto> onBoardNewDoctor(@RequestBody NewDoctorRequestDto newDoctorRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorservice.onBoardNewDoctor(newDoctorRequestDto));
    }
}

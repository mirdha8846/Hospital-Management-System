package com.example.hms.service;

import com.example.hms.entity.Doctor;
import com.example.hms.repo.Doctorrepo;
import com.example.hms.repo.Userrepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Doctorservice {
private final Doctorrepo doctorrepo;
private final Userrepo userrepo;

public List<Doctor> getAllDoctors(){
    return doctorrepo.findAll();
}
@Transactional
    public void onBoardNewDoctor( Doctor newdoctor){

//later we complete it
}
}

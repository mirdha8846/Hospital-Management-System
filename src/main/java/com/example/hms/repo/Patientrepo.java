package com.example.hms.repo;

import com.example.hms.dto.BloodGroupResponsType;
import com.example.hms.entity.Patient;
import com.example.hms.entity.type.BloodGroupType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface Patientrepo extends JpaRepository<Patient,Long> {
    Patient findByName(String name);


    List<Patient> findByBirthDateOrEmail(LocalDate birthDate,String email);


    @Query("SELECT p from Patient p where p.bloodGroup=?1")
    List<Patient> findByBloodGroup(@Param("bloodGroup")BloodGroupType bloodGroup);


    @Query("SELECT new com.example.hms.dto.BloodGroupResponsType(p.bloodGroup," +
            " Count(p)) from Patient p group by p.bloodGroup ")
    List<BloodGroupResponsType> countEachBloodGroupType();


    @Query(value = "SELECT * from patient",nativeQuery = true)
    Page<Patient>findAllPatitent(Pageable pageable);


    @Transactional
    @Modifying
    @Query("UPDATE Patient p SET p.name = :name where p.id = :id")
    int updateNameWithId(@Param("name") String name, @Param("id") Long id);
}

package com.example.hms.entity;
import com.example.hms.entity.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToOne
    private Doctor headDoctor;

//    @ManyToMany
//    @JoinTable(
//            name = "my_dpt_doctors",
//            joinColumns = @JoinColumn(name = "dpt_id"),
//            inverseJoinColumns = @JoinColumn(name = "doctor_id")
//    )
//    private Set<Doctor> doctors = new HashSet<>();
}

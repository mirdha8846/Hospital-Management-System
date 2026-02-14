package com.example.hms.security;

import com.example.hms.dto.LoginRequestDto;
import com.example.hms.dto.LoginResponseDto;
import com.example.hms.dto.SignUpRequestDto;
import com.example.hms.dto.SignupResponseDto;
import com.example.hms.entity.Patient;
import com.example.hms.entity.User;
import com.example.hms.entity.type.Authprovidertype;
import com.example.hms.repo.Patientrepo;
import com.example.hms.repo.Userrepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Authservice {
    private final AuthenticationManager authenticationManager;
    private final Authutil authutil;
    private final ModelMapper modelMapper;
    private final Userrepo userrepo;
    private final Patientrepo patientrepo;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())

        );

        User user= (User) authentication.getPrincipal();
        String acessToken= authutil.generateAccessToken(user);
        return new LoginResponseDto(acessToken, user.getId() );
    }

    public SignupResponseDto signup(SignUpRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, Authprovidertype.EMAIL, null);
       return modelMapper.map(user,SignupResponseDto.class);
//        return new SignupResponseDto(user.getId(), user.getUsername());
    }
    public User signUpInternal(SignUpRequestDto signupRequestDto, Authprovidertype authProviderType, String providerId) {
        User user = userrepo.findByUsernam(signupRequestDto.getUsername()).orElse(null);

        if(user != null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .roles(signupRequestDto.getRoles()) // Role.PATIENT
                .build();

        if(authProviderType == Authprovidertype.EMAIL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        user = userrepo.save(user);

        Patient patient = Patient.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getUsername())
                .user(user)
                .build();
        patientrepo.save(patient);

        return user;
    }
}

package com.example.hms.security;

import com.example.hms.dto.LoginRequestDto;
import com.example.hms.dto.LoginResponseDto;
import com.example.hms.dto.SignUpRequestDto;
import com.example.hms.dto.SignupResponseDto;
import com.example.hms.entity.Patient;
import com.example.hms.entity.User;
import com.example.hms.entity.type.Authprovidertype;
import com.example.hms.entity.type.RoleType;
import com.example.hms.repo.Patientrepo;
import com.example.hms.repo.Userrepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class Authservice {
    private final AuthenticationManager authenticationManager;
    private final Authutil authutil;
    private final ModelMapper modelMapper;
    private final Userrepo userrepo;
    private final Patientrepo patientrepo;
    private final PasswordEncoder passwordEncoder;
    private final Authprovidertype authprovidertype;

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

    public ResponseEntity<LoginResponseDto> oAuth2login(OAuth2User oAuth2User, String registrationId) {
        Authprovidertype providerType = authutil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authutil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userrepo.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User emailUser = userrepo.findByUsernam(email).orElse(null);

        if(user == null && emailUser == null) {
            // signup flow:
            String username = authutil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signUpInternal(new SignUpRequestDto(username, null, name, Set.of(RoleType.PATIENT)),
                    authprovidertype, providerId);
        } else if(user != null) {
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userrepo.save(user);
            }
        } else {
            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(authutil.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);
    }

    }


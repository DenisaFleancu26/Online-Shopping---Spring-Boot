package com.example.eCommerceApp.service;
import com.example.eCommerceApp.dto.ChangePasswordRequest;
import com.example.eCommerceApp.model.User;
import com.example.eCommerceApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eCommerceApp.exception.ResourceNotFoundException;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public User registerUser(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        user.setConfirmationCode(generateConfirmationCode());
        user.setEmailConfirmation(false);
        emailService.sendConfirmationCode(user);

        return userRepository.save(user);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void changePassword(String email, ChangePasswordRequest request){
        User user = getUserByEmail(email);
        if(!passwordEncoder.matches(user.getPassword(), request.getCurrentPassword())){
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void confirmEmail(String email, String confirmationCode){
        User user = getUserByEmail(email);
        if(user.getConfirmationCode().equals(confirmationCode)){
            user.setEmailConfirmation(true);
            user.setConfirmationCode(null);
            userRepository.save(user);
        }else{
            throw new BadCredentialsException("Invalid confirmation code");
        }
    }

    private String generateConfirmationCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

}

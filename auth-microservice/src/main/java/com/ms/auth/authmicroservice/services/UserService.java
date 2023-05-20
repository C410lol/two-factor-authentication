package com.ms.auth.authmicroservice.services;

import com.ms.auth.authmicroservice.dtos.AuthDto;
import com.ms.auth.authmicroservice.models.UserModel;
import com.ms.auth.authmicroservice.repositories.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel save(@NotNull UserModel userModel) {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    public void saveVerified(@NotNull UserModel userModel) {
        userRepository.save(userModel);
    }

    public boolean authenticate(@NotNull AuthDto authDto) {
        var optionalUserModel = userRepository.findByUsername(authDto.getUsername());
        if(optionalUserModel.isPresent()) {
            if(passwordEncoder.matches(authDto.getPassword(),
                    optionalUserModel.get().getPassword())) {
                return optionalUserModel.get().isVerified();
            }
        }
        return false;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public void deleteById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

}

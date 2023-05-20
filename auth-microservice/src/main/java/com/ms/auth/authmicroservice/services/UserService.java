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
        userModel.setVerificationCode(passwordEncoder.encode(userModel.getVerificationCode()));
        return userRepository.save(userModel);
    }

    public void saveVerified(@NotNull UserModel userModel) {
        userRepository.save(userModel);
    }

    public boolean authenticate(@NotNull AuthDto authDto) {
        var optionalUserModel = userRepository.findByUsername(authDto.getUsername());
        return optionalUserModel.filter(userModel -> passwordEncoder.matches(
                authDto.getPassword(), userModel.getPassword())).isPresent();
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

    public boolean isUserVerified(String username) {
        var optionalUserModel = userRepository.findByUsername(username);
        return optionalUserModel.map(UserModel::isVerified).orElse(false);
    }

    public boolean isSameVerificationCode(String rawVerificationCode, String encodedVerificationCode) {
        return passwordEncoder.matches(rawVerificationCode, encodedVerificationCode);
    }

}

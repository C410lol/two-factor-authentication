package com.ms.auth.authmicroservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ms.auth.authmicroservice.dtos.AuthDto;
import com.ms.auth.authmicroservice.dtos.UserDto;
import com.ms.auth.authmicroservice.dtos.VerificationMailDto;
import com.ms.auth.authmicroservice.models.UserModel;
import com.ms.auth.authmicroservice.services.JwtService;
import com.ms.auth.authmicroservice.services.MailService;
import com.ms.auth.authmicroservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<UserModel> create(@RequestBody @Valid UserDto userDto) throws JsonProcessingException {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        var verificationCode = new Random().nextInt(100000);
        userModel.setVerificationCode(verificationCode);
        userModel.setVerified(false);
        mailService.sendMailVerificationMessage(new VerificationMailDto(
                userDto.getEmail(),
                verificationCode
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> listAll() {
        var userModelList = userService.findAll();
        if(userModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found :(");
        }
        return ResponseEntity.ok(userModelList);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> listOne(@PathVariable(value = "uuid") UUID uuid) {
        var optionalUserModel = userService.findById(uuid);
        return optionalUserModel.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found :("));
    }

    @PutMapping("/edit/{uuid}")
    public ResponseEntity<Object> edit(@PathVariable(value = "uuid") UUID uuid, @RequestBody @Valid UserDto userDto) {
        var optionalUserModel = userService.findById(uuid);
        if(optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found :(");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUuid(optionalUserModel.get().getUuid());
        userModel.setVerificationCode(optionalUserModel.get().getVerificationCode());
        userModel.setVerified(optionalUserModel.get().getVerified());
        return ResponseEntity.ok(userService.save(userModel));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<Object> delete(@PathVariable(value = "uuid") UUID uuid) {
        var optionalUserModel = userService.findById(uuid);
        if(optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found :(");
        }
        userService.deleteById(uuid);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @PostMapping("/verify/{uuid}")
    public ResponseEntity<Object> verify(@PathVariable(value = "uuid") UUID uuid,
                                         @RequestParam(value = "verificationCode") Integer verificationCode) {
        var optionalUserModel = userService.findById(uuid);
        if(optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found :(");
        }
        if(!optionalUserModel.get().getVerificationCode().equals(verificationCode)) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                    "Error verifying code, it might be wrong :/");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(optionalUserModel.get(), userModel);
        userModel.setVerified(true);
        userService.saveVerified(userModel);
        return ResponseEntity.ok("Thanks for verifying your account ;)");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDto authDto) {
        if(!userService.authenticate(authDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Something gone wrong :/, try again later or check your credentials");
        }
        return ResponseEntity.ok(jwtService.generateToken(authDto.getUsername()));
    }

}

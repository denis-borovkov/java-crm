package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.entity.UpdatePasswordRequest;
import com.denisborovkov.javacrm.dto.entity.UserDTO;
import com.denisborovkov.javacrm.entity.UserEntity;
import com.denisborovkov.javacrm.exception.auth.PasswordMismatchException;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id){
        UserEntity userEntity = userService.getUserById(id);
        return ResponseEntity.ok().body(userMapper.toDTO(userEntity));
    }

    @GetMapping({"/user/{username}"})
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){
        UserEntity userEntity = userService.getUserByEmail(username);
        return ResponseEntity.ok().body(userMapper.toDTO(userEntity));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok().body(userService.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request)
            throws PasswordMismatchException {
        userService.changePassword(request.id(),
                request.oldPassword(),
                request.newPassword());
        return ResponseEntity.ok().body("Successfully updated");
    }
}

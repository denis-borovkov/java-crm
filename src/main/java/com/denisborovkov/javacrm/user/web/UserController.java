package com.denisborovkov.javacrm.user.web;

import com.denisborovkov.javacrm.user.dto.UpdatePasswordRequest;
import com.denisborovkov.javacrm.user.dto.UserDTO;
import com.denisborovkov.javacrm.user.domain.UserEntity;
import com.denisborovkov.javacrm.auth.exception.PasswordMismatchException;
import com.denisborovkov.javacrm.user.mapper.UserMapper;
import com.denisborovkov.javacrm.user.service.UserService;
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

    @GetMapping("/id")
    public ResponseEntity<UserDTO> get(@RequestParam Long id){
        UserEntity userEntity = userService.getUserById(id);
        return ResponseEntity.ok().body(userMapper.toDTO(userEntity));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> get(@RequestParam String username){
        UserEntity userEntity = userService.getUserByEmail(username);
        return ResponseEntity.ok().body(userMapper.toDTO(userEntity));
    }

    @GetMapping("/all")
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




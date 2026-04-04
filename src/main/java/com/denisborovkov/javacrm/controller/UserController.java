package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.UpdatePasswordRequest;
import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.exception.PasswordMismatchException;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.entity.User;
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
    public UserDTO getById(@PathVariable Long id){
        return userMapper.toDTO(userService.getUserById(id));
    }

    @GetMapping({"/user/{username}"})
    public UserDTO getByUsername(@PathVariable String username){
        User user = (User) userService.loadUserByUsername(username);
        return userMapper.toDTO(user);
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody UpdatePasswordRequest request) throws PasswordMismatchException {
        userService.updatePassword(request.id(),
                request.oldPassword(),
                request.newPassword());
        return ResponseEntity.ok().body("Successfully updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAll(){
        userService.deleteAllUsers();
        return ResponseEntity.ok().body("Successfully deleted");
    }
}

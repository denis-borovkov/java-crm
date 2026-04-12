package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.auth.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.entity.UserDTO;
import com.denisborovkov.javacrm.service.AuthService;
import com.denisborovkov.javacrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok().body(authService.registerAdmin(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}

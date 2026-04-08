package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.service.AuthService;
import com.denisborovkov.javacrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok().body(authService.registerAdmin(request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAll(){
        userService.deleteAllUsers();
        return ResponseEntity.ok().body("Successfully deleted");
    }
}

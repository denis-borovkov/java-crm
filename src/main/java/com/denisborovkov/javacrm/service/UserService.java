package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.auth.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.auth.SignupRequest;
import com.denisborovkov.javacrm.entity.UserEntity;
import com.denisborovkov.javacrm.enums.Role;
import com.denisborovkov.javacrm.exception.auth.PasswordMismatchException;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.repository.UserRepository;
import com.denisborovkov.javacrm.security.UserPrincipal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserEntity createUser(SignupRequest request) {
        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public UserEntity createAdmin(CreateAdminRequest request) {
        UserEntity admin = userMapper.toEntity(request);
        admin.setPassword(passwordEncoder.encode(request.password()));
        admin.setRole(Role.ADMIN);
        return userRepository.save(admin);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) throws PasswordMismatchException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new PasswordMismatchException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new UserPrincipal(user);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public void deleteUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
        userRepository.delete(user);
    }

    public boolean existsUserByEmail(@Email @NotBlank String email) {
        return userRepository.existsByEmail(email);
    }
}

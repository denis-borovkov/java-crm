package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.SignupRequest;
import com.denisborovkov.javacrm.entity.UserEntity;
import com.denisborovkov.javacrm.enums.Role;
import com.denisborovkov.javacrm.exception.PasswordMismatchException;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
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
        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.password()));
        userEntity.setRole(Role.USER);
        return userRepository.save(userEntity);
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
                -> new UsernameNotFoundException("UserEntity not found"));
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) throws PasswordMismatchException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + id));
        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
                throw new PasswordMismatchException();
        }
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    @Transactional
    public void updatePasswordByEmail(String email, String newPassword) {
        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + email));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + email));

        return User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();
    }

    public void deleteUserById(Long id) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + id));
        userRepository.delete(userEntity);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public boolean existsUserByEmail(@Email @NotBlank String email) {
        return userRepository.existsByEmail(email);
    }
}

package com.denisborovkov.javacrm.entity;

import com.denisborovkov.javacrm.abstraction.BaseEntity;
import com.denisborovkov.javacrm.enums.CompanyRole;
import com.denisborovkov.javacrm.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private CompanyRole companyRole;
}

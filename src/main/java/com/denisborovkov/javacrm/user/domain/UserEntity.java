package com.denisborovkov.javacrm.user.domain;

import com.denisborovkov.javacrm.common.model.BaseEntity;
import com.denisborovkov.javacrm.user.domain.CompanyRole;
import com.denisborovkov.javacrm.user.domain.Role;
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




package com.denisborovkov.javacrm.entity;

import com.denisborovkov.javacrm.abstraction.BaseEntity;
import com.denisborovkov.javacrm.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
}

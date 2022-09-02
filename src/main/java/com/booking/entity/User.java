package com.booking.entity;

import com.booking.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String mobile;
    private String password;

    public User(String firstName, String lastName, String userName, Role role, String email, String mobile, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.role = role;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }
}

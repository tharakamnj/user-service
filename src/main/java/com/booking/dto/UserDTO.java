package com.booking.dto;
import com.booking.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Role role;
    private String email;
    private String mobile;
    private String password;
}

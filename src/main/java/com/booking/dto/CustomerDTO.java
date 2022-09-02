package com.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long cusId;
    private String userName;
    private String email;
    private String mobile;

    public CustomerDTO(String userName, String email, String mobile) {
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
    }
}

package com.example.demo.Domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

}

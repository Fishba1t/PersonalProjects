package com.example.demo.Domain;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateDTO {
    String currentPassword;
    String newPassword;
}

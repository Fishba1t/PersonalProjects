package com.example.demo.Domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogInDTO {
    public String email;
    public String password;

}

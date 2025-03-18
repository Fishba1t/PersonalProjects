package com.example.demo.Controller;

import com.example.demo.Domain.*;
import com.example.demo.Service.TokenService;
import com.example.demo.Service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@Log4j2


public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        try {
            User user = new User(
                    userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(),
                    userRequest.getPassword(), userRequest.getPhone()
            );
            userService.addUser(user);
            return new ResponseEntity<>("add succes", HttpStatus.OK);
        } catch (DuplicateRequestException e) {
            log.error("trying to add another user with an email that already exists (ERROR)");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "there is already a user with this email!", e);
        }
    }

    @GetMapping("/all")
    public List<UsersDTO> getAllUsers() {
        return userService.getAllUsersDTO();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogInDTO logInDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logInDTO.getEmail(), logInDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenService.tokenGenerator(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("wrong credentials (ERROR)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The inserted user does not exist", e);
        }
    }

    @PutMapping("/newPassword")
    public ResponseEntity<String> updateUserPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        try {
            String token = bearerToken.substring(7);
            String userEmail = this.tokenService.getUsernameFromJWT(token);
            User user = this.userService.findByEmail(userEmail);
            this.userService.updatePassword(user, passwordUpdateDTO);
            return new ResponseEntity<>("User password updated succesfully", HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password Missmatch");
        }
    }

}

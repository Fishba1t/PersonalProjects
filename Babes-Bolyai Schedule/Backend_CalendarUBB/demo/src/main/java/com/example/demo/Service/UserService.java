package com.example.demo.Service;

import com.example.demo.Domain.PasswordUpdateDTO;
import com.example.demo.Domain.User;
import com.example.demo.Domain.UsersDTO;
import com.example.demo.Repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(long id) throws NoSuchElementException {
        return userRepository.findById(id).get();
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void addUser( User user) {
        userRepository.findAll().forEach(u -> {
            if (u.getId() == user.getId() ||
                    u.getEmail().contentEquals(user.getEmail()))
                throw new DuplicateRequestException();
        });
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<UsersDTO> getAllUsersDTO() {
        List<UsersDTO> usersDTOS = new ArrayList<>();
        for (var u : this.getAllUsers()) {
            usersDTOS.add(new UsersDTO(u.getFirstName(), u.getLastName(), u.getEmail()));
        }
        return usersDTOS;
    }
    @Transactional
    public void updatePassword(User user, PasswordUpdateDTO passwordUpdateDTO) throws AuthenticationException {
        if(this.passwordEncoder.matches(passwordUpdateDTO.getCurrentPassword(), user.getPassword())){
            this.userRepository.updatePassword(user.getId(), passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        }
        else throw new AuthenticationException("Password missmatch");
    }
}

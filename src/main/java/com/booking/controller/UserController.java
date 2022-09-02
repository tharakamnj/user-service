package com.booking.controller;

import com.booking.dto.UserDTO;
import com.booking.entity.User;
import com.booking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto){
        return userService.createUser(dto);
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        dto.setUserId(id);
        return userService.UpdateUsers(dto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userService.deleteUsers(id);
    }

    @GetMapping("/user/{userName}")
    public User getUserByUserName(@PathVariable("userName") String userName){
        return userService.getUserByUserName(userName);
    }


}
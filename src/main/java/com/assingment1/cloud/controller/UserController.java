package com.assingment1.cloud.controller;

import com.assingment1.cloud.model.User;
import com.assingment1.cloud.Repository.UserRepository;
import com.assingment1.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(method= RequestMethod.POST, value="/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        // check if user is present
        ResponseEntity<User> responseEntity;
        if(user.getId() == null &&  user.getAccount_created() == null && user.getAccount_updated() == null) {
            try {
                User user1 = userService.saveUser(user);
                responseEntity = new ResponseEntity<User>(user1, HttpStatus.CREATED);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                responseEntity = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
            }
        } else {
            responseEntity = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("/user/self")
    public User getUser(Authentication authentication ) {
        return userRepository.findUserByEmailaddress(authentication.getName()).get();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/self")
    public ResponseEntity<User> updateUser(@RequestBody User user, Authentication authentication) {
        return userService.updateUser(user, authentication);
    }

}
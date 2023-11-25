package org.example.server.controllers;

import org.example.server.models.User;
import org.example.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User("purple", "purple@gmail.com","hash", false));
        return users;
//        return userService.getUsers();
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user){

    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){

    }


}

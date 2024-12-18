package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.UserDTO;
import com.example.tradingjournal.HardCodedValues;
import com.example.tradingjournal.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/viewUsers", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> viewUserByUserName(@RequestParam("username") String userName) {
        Optional<UserDTO> user = userService.findByName(userName);
        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/viewAllUsers", method = RequestMethod.GET)
    public ResponseEntity<Iterable<UserDTO>> viewAllUsers() {
        Iterable<UserDTO> users = userService.findAll();
        if(users.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO user)
    {
       UserService.validateUser(user);
       UserDTO savedUser = userService.save(user);
       return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public ResponseEntity<String> deleteUserByUsername(@RequestParam("username") String username) {
        if(userService.deleteByName(username) > 0)
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
    }
}

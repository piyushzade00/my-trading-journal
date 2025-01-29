package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.OTPDetails;
import com.example.tradingjournal.DTO.UserDTO;
import com.example.tradingjournal.HardCodedValues;
import com.example.tradingjournal.JwtResponse;
import com.example.tradingjournal.JwtUtil;
import com.example.tradingjournal.Service.AuthenticationService;
import com.example.tradingjournal.Service.EmailService;
import com.example.tradingjournal.Service.OTPService;
import com.example.tradingjournal.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final OTPService otpService;
    private final EmailService emailService;

    private Map<String, OTPDetails> otpStorage = new HashMap<>();

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, OTPService otpService, EmailService emailService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/viewUser", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> viewUserByEmail(@RequestParam("email") String email) {
        Optional<UserDTO> user = userService.findByEmail(email);
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
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDTO user)
    {
        Map<String, String> response = new HashMap<>();

       UserService.validateUser(user);
       String userEmail = user.getEmailAddress();
       UserDTO savedUser = userService.save(user);

       if(savedUser != null) {
           String otp = otpService.generateOTP();
           OTPDetails otpDetails = new OTPDetails(otp, LocalDateTime.now());
           otpStorage.put(userEmail, otpDetails);

           emailService.sendOTP(userEmail, otp);
           response.put("message", "OTP sent successfully");
           response.put("status", "success");
           emailService.sendWelcomeEmail(userEmail);

           return ResponseEntity.ok(response);
       }
        response.put("message", "User could not be saved");
        response.put("status", "error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        Map<String, String> response = new HashMap<>();
        OTPDetails otpDetails = otpStorage.get(email);

        if (otpDetails == null) {
            response.put("message", "No OTP found for this email");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime generatedTime = otpDetails.getTimestamp();
        long minutesElapsed = java.time.Duration.between(generatedTime, now).toMinutes();

        if (minutesElapsed > 5) {
            otpStorage.remove(email);
            response.put("message", "OTP has expired. Please request a new one.");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (otpDetails.getOtp().equals(otp)) {
            otpStorage.remove(email);
            response.put("message", "OTP verified successfully");
            response.put("status", "success");
            Optional<UserDTO> user = userService.findByEmail(email);
            if(user.isPresent()) {
                String accessToken = JwtUtil.generateToken(email);
                String refreshToken = JwtUtil.generateRefreshToken(email);
                authenticationService.saveRefreshTokenToDB(user.get(), refreshToken);
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);
            }
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid OTP");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changeUserPassword(@RequestParam String email, @RequestParam String password) {
        Map<String, String> response = new HashMap<>();
        int isPasswordChanged = userService.changePasswordInDB(email, password);

        if (isPasswordChanged > 0) {
            response.put("message", "Password changed successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Unable to change password.");
        response.put("status", "error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping(value = "/updateUser")
    public ResponseEntity<Map<String,String>> updateUserInfo(@RequestBody UserDTO user) {
        Map<String,String> response = new HashMap<>();

        if(UserService.isValidUserName(user.getUserName()))
        {
            if(userService.updateUserInfo(user) > 0) {
                response.put("message", "User updated successfully");
                response.put("status", "success");
                return ResponseEntity.ok(response);
            }
        }
        response.put("message", "User's name is not valid.");
        response.put("status", "error");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping(value = "/deleteUser")
    public ResponseEntity<Map<String,String>> deleteUserByEmail(@RequestParam("email") String email) {
        Map<String,String> response = new HashMap<>();
        if(userService.deleteByEmail(email) > 0)
        {
            response.put("message", "User deleted successfully");
            response.put("status", "success");
            emailService.sendUserAccountDeletedEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
            response.put("message", "User could not be deleted.");
            response.put("status", "error");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

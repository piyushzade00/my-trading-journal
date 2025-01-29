package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.OTPDetails;
import com.example.tradingjournal.DTO.RefreshTokenDTO;
import com.example.tradingjournal.DTO.UserDTO;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class UserAuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    private Map<String, OTPDetails> otpStorage = new HashMap<>();

    @Autowired
    public UserAuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @RequestMapping(value = "/authenticate_login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> authenticateLogin(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        String email = userDTO.getEmailAddress();

        Optional<UserDTO> user = authenticationService.authenticateUser(email, userDTO.getPassword());
        if(user.isPresent()) {
            String otp = otpService.generateOTP();
            OTPDetails otpDetails = new OTPDetails(otp, LocalDateTime.now());
            otpStorage.put(email, otpDetails);

            // Send OTP email
            emailService.sendOTP(email, otp);
            response.put("message", "OTP sent successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        response.put("message", "No user exists with this email.");
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
               String refreshToken;
               Optional<RefreshTokenDTO> refreshTokenDTO = authenticationService.getRefreshToken(email);
               if(refreshTokenDTO.isPresent())
                   refreshToken = refreshTokenDTO.get().getRefreshToken();
               else
               {
                   refreshToken = JwtUtil.generateRefreshToken(email);
                   authenticationService.saveRefreshTokenToDB(user.get(), refreshToken);
               }
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

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPasswordOTP(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        logger.info("From forgot-password endpoint");
        logger.info("Email from forgot-password : {}",email);
        Optional<UserDTO> user = authenticationService.isUserPresent(email);
        if(user.isPresent()) {
            String otp = otpService.generateOTP();
            OTPDetails otpDetails = new OTPDetails(otp, LocalDateTime.now());
            otpStorage.put(email, otpDetails);

            // Send OTP email
            emailService.sendOTP(email, otp);
            response.put("message", "OTP sent successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        response.put("message", "User does not exist. Please Sign up.");
        response.put("status", "error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }

    @PostMapping("/renew_access_token")
    public ResponseEntity<JwtResponse> renewAccessToken(@RequestBody RefreshTokenDTO refreshToken) {
        String newAccessToken = authenticationService.renewAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(new JwtResponse(newAccessToken));
    }

    @PostMapping("/invalidate_refresh_token")
    public ResponseEntity<String> invalidateRefreshToken(@RequestBody RefreshTokenDTO refreshToken) {
        authenticationService.invalidateToken(refreshToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
}

package com.example.tradingjournal.Service;

import com.example.tradingjournal.DAO.UserRepository;
import com.example.tradingjournal.DTO.UserDTO;
import com.example.tradingjournal.ServiceInterface;
import com.example.tradingjournal.HardCodedValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements ServiceInterface<UserDTO> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        return userRepository.save(userDTO);
    }

    @Override
    public Optional<UserDTO> findByName(String name) {
        return userRepository.findByUserName(name);
    }

    public Optional<UserDTO> findUserByUserId(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Iterable<UserDTO> findAll() {
        return userRepository.findAll();
    }

    @Override
    public int deleteByName(String name) {
        return userRepository.deleteByUserName(name);
    }

    public static void validateUser(UserDTO user) throws ResponseStatusException {
        if(!isValidUserName(user.getUserName()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,HardCodedValues.Strings.USERNAME_INVALID);
        }
        else if(!isValidEmail(user.getEmailAddress()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,HardCodedValues.Strings.EMAIL_INVALID);
        }
        else if(!isValidPassword(user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,HardCodedValues.Strings.PASSWORD_INVALID);
        }
    }

    public static boolean isValidUserName(String userName) {
        return (userName == null || userName.trim().isEmpty()) ? false : true;
    }

    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile(HardCodedValues.Strings.EMAIL_REGEX);
        return (email == null || email.trim().isEmpty()) ? false :  emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile(HardCodedValues.Strings.PASSWORD_REGEX);
        return (password == null || password.trim().isEmpty()) ? false : passwordPattern.matcher(password).matches();
    }
}

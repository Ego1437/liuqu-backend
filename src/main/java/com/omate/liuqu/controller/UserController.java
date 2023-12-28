package com.omate.liuqu.controller;

import com.omate.liuqu.dto.*;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.*;
import com.omate.liuqu.service.*;
import com.omate.liuqu.utils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/api") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * *
     * 
     * @param user
     * @return userDTO
     */
    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(user.getUid());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setHobby(user.getHobby());
        userDTO.setGender(user.getGender());
        userDTO.setPersonalDescription(user.getPersonalDescription());
        userDTO.setUserType(user.getUserType());
        return userDTO;
    }

    @PostMapping(value = "/register", consumes = { "multipart/form-data" })
    public ResponseEntity<Result> register(@Valid User user) {
        Result result = userService.register(user);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestParam String email, @RequestParam String password) {

        String hashedPassword = passwordEncoder.encode(password);

        User user = userRepository.findByEmail(email).orElse(null);
        Result result = new Result();

        if (user != null && passwordEncoder.matches(password, user.getPassword())) { // Encrypted passwords should
                                                                                     // be used in practical
                                                                                     // applications
            UserDTO userDTO = convertToDto(user);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user",userDTO);

            result.setResultSuccess(0, resultMap);

            HttpHeaders headers = new HttpHeaders();
            Date exp = new Date();
            exp.setTime(exp.getTime() + 1000 * 60 * 60 * 24 * 7); // 7 days
            Map<String, Object> data = Map.of("user", userDTO);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWTManager.createToken(exp, data));
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);

            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } else {
            result.setResultFailed(1);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users/me")
    public ResponseEntity<Result> getUserByAuthToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Result result = new Result();
        if (token == null || token.isEmpty() || !token.contains("Bearer ")) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        token = token.replace("Bearer ", "");

        UserDTO userDTO = JWTManager.getDataFromToken(token, "user", UserDTO.class);

        if (userDTO == null) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        UserDTO updatedUserDTO = convertToDto(userRepository.findById(userDTO.getUid()).orElse(null));

        if (updatedUserDTO == null) {
            result.setResultFailed(3);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user",updatedUserDTO);
        result.setResultSuccess(0, resultMap);

        HttpHeaders headers = new HttpHeaders();
        Date exp = new Date();
        exp.setTime(exp.getTime() + 1000 * 60 * 60 * 24 * 7); // 7 days
        Map<String, Object> data = Map.of("user", updatedUserDTO);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWTManager.createToken(exp, data));
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);

        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Result> forgotPassword(@RequestParam String emailOrPhone) {
        User user = userRepository.findByEmail(emailOrPhone)
                .orElse((User) userRepository.findByPhone(emailOrPhone).orElse(null));

        if (user != null) {
            String token = JWTManager.createToken(new Date(System.currentTimeMillis() + 1000 * 60 * 15), // 15 minutes
                    Map.of("uid", user.getUid()));

            String resetPasswordLink = "%BASE_URL%/reset-password/" + token + "/";

            try {
                EmailManager.sendEmail(emailOrPhone,
                        "Reset password for " + user.getUsername(),
                        "Hello " + user.getUsername() + "!" + "<br><br>" +
                                " Please follow this link to reset your password: " + "<br>" +
                                " <a href=\"" + resetPasswordLink + "\">" + resetPasswordLink + "</a>" + "<br><br>" +
                                " The link will expire in 15 minutes." + "<br><br>" +
                                " Do NOT share this link with anyone." + "<br><br>" +
                                " If you did not request a password reset, please ignore this email.");
            } catch (Exception e) {
                e.printStackTrace();

                Result result = new Result();
                result.setResultFailed(5);
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Result result = new Result();
        result.setResultSuccess(7);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/validate-reset-password-token")
    public ResponseEntity<Result> validateResetPasswordToken(@RequestParam String resetPasswordToken) {
        Result result = new Result();
        if (resetPasswordToken == null || resetPasswordToken.isEmpty()) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        int uid = JWTManager.getDataFromToken(resetPasswordToken, "uid", Integer.class);

        if (uid == 0) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        UserDTO userDTO = convertToDto(userRepository.findById(uid).orElse(null));

        if (userDTO == null) {
            result.setResultFailed(3);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user",userDTO);

        result.setResultSuccess(0, resultMap);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/change-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String email, @RequestParam String password){
        Result result = new Result();
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Result> resetPassword(@RequestParam String resetPasswordToken, @RequestParam String password,
            @RequestParam String confirmPassword) {
        Result result = new Result();

        if (!password.equals(confirmPassword)) {
            result.setResultFailed(6);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        int uid = JWTManager.getDataFromToken(resetPasswordToken, "uid", Integer.class);

        if (uid == 0) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(uid).orElse(null);

        if (user == null) {
            result.setResultFailed(3);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        result.setResultSuccess(0);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @GetMapping("users/{id}")
    public ResponseEntity<Result> getUserById(@PathVariable Integer id) {
        Result result = new Result();

        Map<String, Object> resultMap = new HashMap<>();
        UserDTO userDTO = userService.findUserById(id);

        if(userDTO != null){

            resultMap.put("user",userDTO);
            result.setResultSuccess(0, resultMap);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.setResultFailed(3);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }


    }
    @PutMapping(value = "/users/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Result> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Integer id,User updatedUser) {
        Result result = new Result();

        if (token == null || token.isEmpty() || !JWTManager.checkToken(token.substring(7), id)) {
            result.setResultFailed(4);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        User user = userService.updateUser(id, updatedUser);

        UserDTO userDTO = convertToDto(user);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user",userDTO);
        result.setResultSuccess(0, resultMap);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
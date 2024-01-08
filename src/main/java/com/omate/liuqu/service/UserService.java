package com.omate.liuqu.service;

import com.omate.liuqu.dto.UserDTO;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

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
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserTel(user.getUserTel());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setAvatarPath(user.getAvatarPath());
        userDTO.setPostcode(user.getPostcode());
        userDTO.setIsSubscribe(user.getIsSubscribe());
        return userDTO;
    }

    /**
     * register
     *
     * @param user parameter encapsulation
     * @return Result
     */
    public Result register(User user) {
        Result result = new Result();
        // Define a regex pattern for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getUserEmail());
        // Check if email format is valid
        if (!matcher.matches()) {
            result.setResultFailed(9);
            return result;
        }
        Optional<User> getUser = userRepository.findByUserEmail(user.getUserEmail());
        if (getUser.isPresent()) {
            result.setResultFailed(2);
            return result;
        }
        // encode password by Bcrypt
        String plainPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        UserDTO userDTO = convertToDto(user);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user",userDTO);
        result.setResultSuccess(0, resultMap);
        return result;

    }

    public Result login(String emailOrPhone, String plainPassword) {

        Result result = new Result();
        // Encode login password
        String hashedPassword = passwordEncoder.encode(plainPassword);
        // validate password
        User user = userRepository.findByUserEmail(emailOrPhone)
                .orElse((User) userRepository.findByUserTel(emailOrPhone).orElse(null));
        if (user != null && passwordEncoder.matches(plainPassword, user.getPassword())) { // Encrypted passwords should
                                                                                          // be used in practical
                                                                                          // applications
            UserDTO userDTO = convertToDto(user);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user",userDTO);
            result.setResultSuccess(0, resultMap);

        } else {
            result.setResultFailed(1);
        }

        return result;
    }



    public UserDTO findUserById(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        if (user != null) {

            UserDTO userDTO = convertToDto(user);

            return userDTO;
        }else{
            return null;
        }

    }

    public User findUserByEmail(String email) {
        User user = userRepository.findByUserEmail(email).orElse(null);
        return user;

    }
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (updatedUser.getAddress() != null && !updatedUser.getAddress().isEmpty()) {
                        user.setAddress(updatedUser.getAddress());
                    }

                    if (updatedUser.getAvatarPath() != null && !updatedUser.getAvatarPath().isEmpty()) {
                        user.setAvatarPath(updatedUser.getAvatarPath());
                    }

                    if (updatedUser.getUserEmail() != null && !updatedUser.getUserEmail().isEmpty()) {
                        user.setUserEmail(updatedUser.getUserEmail());
                    }

                    if (updatedUser.getUserTel() != null && !updatedUser.getUserTel().isEmpty()) {
                        user.setUserTel(updatedUser.getUserTel());
                    }

                    if (updatedUser.getUserName() != null && !updatedUser.getUserName().isEmpty()) {
                        user.setUserName(updatedUser.getUserName());
                    }

                    if (updatedUser.getGender() != null && !updatedUser.getGender().isEmpty()) {
                        user.setGender(updatedUser.getGender());
                    }

                    if (updatedUser.getPostcode() != null) {
                        user.setPostcode(updatedUser.getPostcode());
                    }

                    if (updatedUser.getIsSubscribe() != null) {
                        user.setIsSubscribe(updatedUser.getIsSubscribe());
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

}

package com.catskhi.user.service;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }

    public UserModel updateUser(String id, UserModel userDetails) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public UserModel deleteUser(String id) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return user;
    }
}

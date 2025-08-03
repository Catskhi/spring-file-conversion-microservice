package com.catskhi.user.service;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.producer.UserProducer;
import com.catskhi.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public UserModel getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserModel saveUser(UserModel user) {
        userProducer.sendUserCreateEvent(user);
        return userRepository.save(user);
    }

    public UserModel updateUser(UUID id, UserModel userDetails) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public UserModel deleteUser(UUID id) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return user;
    }
}

package com.catskhi.user.service;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.dto.RegisterDto;
import com.catskhi.user.exception.DataNotFoundException;
import com.catskhi.user.exception.EmailAlreadyExistsException;
import com.catskhi.user.producer.UserProducer;
import com.catskhi.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + id));
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
        UserModel user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public UserModel registerUser(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + registerDto.email());
        }
        UserModel newUser = new UserModel();
        newUser.setName(registerDto.name());
        newUser.setEmail(registerDto.email());
        newUser.setPassword(new BCryptPasswordEncoder().encode(registerDto.password()));
        UserModel savedUser = userRepository.save(newUser);
        userProducer.sendUserCreateEvent(savedUser);
        return savedUser;
    }

    public UserModel deleteUser(UUID id) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        return user;
    }
}
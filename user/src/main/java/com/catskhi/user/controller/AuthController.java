package com.catskhi.user.controller;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.dto.LoginDto;
import com.catskhi.user.dto.RegisterDto;
import com.catskhi.user.dto.TokenDto;
import com.catskhi.user.dto.UserDto;
import com.catskhi.user.service.TokenService;
import com.catskhi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterDto registerDto) {
        UserModel user = userService.registerUser(registerDto);
        UserDto savedUserDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.status(201).body(savedUserDto);
    }
}

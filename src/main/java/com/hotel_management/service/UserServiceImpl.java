package com.hotel_management.service;

import com.hotel_management.dto.request.AuthRequest;
import com.hotel_management.dto.request.UserRequest;
import com.hotel_management.dto.response.AuthResponse;
import com.hotel_management.dto.response.UserResponse;
import com.hotel_management.entity.User;
import com.hotel_management.exception.UserAlreadyFoundException;
import com.hotel_management.exception.UserNotFoundException;
import com.hotel_management.repository.UserRepository;
import com.hotel_management.security.CustomUserDetailsService;
import com.hotel_management.utils.JwtUtil;
import com.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserResponse registerUser(UserRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyFoundException("User already exists with email address: " + request.getEmail());
            }
            User user = Utils.mapUserRequestToUserEntity(request);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            User savedUser = userRepository.save(user);
            return Utils.mapUserEntityToUserResponse(user);

        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public AuthResponse loginUser(AuthRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails userDetails = customUserDetailsService.
                    loadUserByUsername(request.getEmail());
            final String token = jwtUtil.generateToken(userDetails);
            final String role = getUserRole(request.getEmail());
            return AuthResponse.builder()
                    .statusCode(200)
                    .message("Successful")
                    .token(token)
                    .role(role)
                    .expirationTime("Your token will expire in 2 hours")
                    .build();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        try {
            return userRepository.findAll()
                    .stream().map(Utils::mapUserEntityToUserResponse).toList();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public UserResponse getUserBookingHistory(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("User not found"));
            return Utils
                    .mapUserEntityToUserResponsePlusUserBookingAndRoom(user);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("User not found"));
            userRepository.delete(user);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public UserResponse getUserById(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("User not found"));
            return Utils.mapUserEntityToUserResponse(user);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public UserResponse getUserInfo(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found with email: " + email));
            return Utils.mapUserEntityToUserResponse(user);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email));
        return user.getRole();
    }
}

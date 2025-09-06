package com.hotel_management.service;

import com.hotel_management.dto.request.AuthRequest;
import com.hotel_management.dto.request.UserRequest;
import com.hotel_management.dto.response.AuthResponse;
import com.hotel_management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    AuthResponse loginUser(AuthRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserBookingHistory(Long userId);

    void deleteUser(Long userId);

    UserResponse getUserById(Long userId);

    UserResponse getUserInfo(String email);

    String getUserRole(String email);
}

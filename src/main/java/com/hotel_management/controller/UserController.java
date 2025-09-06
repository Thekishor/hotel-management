package com.hotel_management.controller;

import com.hotel_management.dto.response.UserResponse;
import com.hotel_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Deleted Successfully");
    }

    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<UserResponse> getLoggedInProfileInfo() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        final String email = authentication.getName();
        UserResponse userResponse = userService.getUserInfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/get-user-booking/{userId}")
    public ResponseEntity<UserResponse> getUserBooking(@PathVariable("userId") Long userId) {
        UserResponse userResponse = userService.getUserBookingHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}

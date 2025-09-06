package com.hotel_management.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String role;

    private List<BookingResponse> bookingResponses = new ArrayList<>();
}

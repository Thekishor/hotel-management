package com.hotel_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
}

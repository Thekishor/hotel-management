package com.hotel_management.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingResponse {

    private Long id;

    private LocalDate checkInData;

    private LocalDate checkOutData;

    private int numberOfAdults;

    private int numberOfChildren;

    private int totalNumberOfGuest;

    private String bookingConfirmationCode;

    private UserResponse userResponse;

    private RoomResponse roomResponse;
}

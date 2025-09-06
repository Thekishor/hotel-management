package com.hotel_management.utils;

import com.hotel_management.dto.request.BookingRequest;
import com.hotel_management.dto.request.RoomRequest;
import com.hotel_management.dto.request.UserRequest;
import com.hotel_management.dto.response.BookingResponse;
import com.hotel_management.dto.response.RoomResponse;
import com.hotel_management.dto.response.UserResponse;
import com.hotel_management.entity.Booking;
import com.hotel_management.entity.Room;
import com.hotel_management.entity.User;

import java.security.SecureRandom;

public class Utils {

    private static final String ALPHANUMERIC_STRING =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static User mapUserRequestToUserEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .build();
    }

    public static UserResponse mapUserEntityToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }

    public static UserResponse mapUserEntityToUserResponsePlusUserBookingAndRoom(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();

        if (!user.getBookings().isEmpty()) {
            userResponse.setBookingResponses(user.getBookings().stream().map(booking ->
                    mapBookingEntityToBookingResponsePlusBookedRoom(booking, false)).toList());
        }
        return userResponse;
    }

    public static BookingResponse mapBookingEntityToBookingResponsePlusBookedRoom(
            Booking booking,
            boolean mapUser
    ) {
        BookingResponse bookingResponse = BookingResponse.builder()
                .id(booking.getId())
                .checkInData(booking.getCheckInDate())
                .checkOutData(booking.getCheckOutDate())
                .numberOfAdults(booking.getNumberOfAdults())
                .numberOfChildren(booking.getNumberOfChildren())
                .totalNumberOfGuest(booking.getTotalNumberOfGuest())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .build();

        if (mapUser) {
            bookingResponse.setUserResponse(Utils.mapUserEntityToUserResponse(booking.getUser()));
        }

        if (booking.getRoom() != null) {
            RoomResponse roomResponse = RoomResponse.builder()
                    .id(booking.getRoom().getId())
                    .roomType(booking.getRoom().getRoomType())
                    .price(booking.getRoom().getPrice())
                    .imgUrl(booking.getRoom().getImgUrl())
                    .description(booking.getRoom().getDescription())
                    .build();
            bookingResponse.setRoomResponse(roomResponse);
        }
        return bookingResponse;
    }

    public static Room mapRoomRequestToRoomEntity(RoomRequest request) {
        return Room.builder()
                .roomType(request.getRoomType())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
    }

    public static RoomResponse mapRoomEntityToRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .imgUrl(room.getImgUrl())
                .description(room.getDescription())
                .build();
    }

    public static RoomResponse mapRoomEntityToRoomResponsePlusBookings(Room room) {
        RoomResponse roomResponse = RoomResponse.builder()
                .id(room.getId())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .imgUrl(room.getImgUrl())
                .description(room.getDescription())
                .build();

        if (room.getBookings() != null) {
            roomResponse.setBookingResponses(room.getBookings().stream().map(Utils::mapBookingEntityToBookingResponse).toList());
        }
        return roomResponse;
    }

    public static BookingResponse mapBookingEntityToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .checkInData(booking.getCheckInDate())
                .checkOutData(booking.getCheckOutDate())
                .numberOfAdults(booking.getNumberOfAdults())
                .numberOfChildren(booking.getNumberOfChildren())
                .totalNumberOfGuest(booking.getTotalNumberOfGuest())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .build();
    }

    public static Booking mapBookingRequestToBooking(BookingRequest request) {
        return Booking.builder()
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfAdults(request.getNumberOfAdults())
                .numberOfChildren(request.getNumberOfChildren())
                .build();
    }
}

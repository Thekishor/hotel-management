package com.hotel_management.service;

import com.hotel_management.dto.request.BookingRequest;
import com.hotel_management.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {

    String saveBooking(Long roomId, Long userId, BookingRequest request);

    BookingResponse findBookingByConfirmationCode(String confirmationCode);

    List<BookingResponse> getAllBookings();

    void cancelBooking(Long bookingId);
}

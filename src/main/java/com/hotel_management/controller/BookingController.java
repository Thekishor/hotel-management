package com.hotel_management.controller;

import com.hotel_management.dto.request.BookingRequest;
import com.hotel_management.dto.response.BookingResponse;
import com.hotel_management.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("book-room/{roomId}/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> saveBookings(
            @PathVariable("roomId") Long roomId,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody BookingRequest request
    ) {
        String confirmationCode = bookingService.saveBooking(roomId, userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(confirmationCode);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBooking() {
        List<BookingResponse> allBookings = bookingService.getAllBookings();
        return ResponseEntity.status(HttpStatus.OK).body(allBookings);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<BookingResponse> getBookingByConfirmationCode(@PathVariable("confirmationCode") String confirmationCode) {
        BookingResponse booking = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body("Booking canceled successfully");
    }

}

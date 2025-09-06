package com.hotel_management.service;

import com.hotel_management.dto.request.BookingRequest;
import com.hotel_management.dto.response.BookingResponse;
import com.hotel_management.entity.Booking;
import com.hotel_management.entity.Room;
import com.hotel_management.entity.User;
import com.hotel_management.exception.RoomNotFoundException;
import com.hotel_management.exception.UserNotFoundException;
import com.hotel_management.repository.BookingRepository;
import com.hotel_management.repository.RoomRepository;
import com.hotel_management.repository.UserRepository;
import com.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final RoomService roomService;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Override
    public String saveBooking(Long roomId, Long userId, BookingRequest request) {
        try {
            if (request.getCheckOutDate().isBefore(request.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("User not found"));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(request, existingBookings)) {
                throw new Exception("Room not available for selected date range");
            }

            Booking booking = Utils.mapBookingRequestToBooking(request);
            booking.setRoom(room);
            booking.setUser(user);
            final String confirmationCode = Utils.generateRandomConfirmationCode(10);
            booking.setBookingConfirmationCode(confirmationCode);
            bookingRepository.save(booking);
            return confirmationCode;
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    @Override
    public BookingResponse findBookingByConfirmationCode(String confirmationCode) {
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            return Utils.mapBookingEntityToBookingResponsePlusBookedRoom(booking, true);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        try {
            return bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                    .stream().map(Utils::mapBookingEntityToBookingResponse).toList();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void cancelBooking(Long bookingId) {
        try {
            bookingRepository.delete(bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found")));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private boolean roomIsAvailable(BookingRequest bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}

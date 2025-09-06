package com.hotel_management.service;

import com.hotel_management.dto.request.RoomRequest;
import com.hotel_management.dto.response.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomResponse addRoom(RoomRequest request, MultipartFile file);

    List<String> getAllRoomTypes();

    List<RoomResponse> getAllRooms();

    void deleteRoom(Long roomId);

    RoomResponse updateRoom(Long roomId, RoomRequest request, MultipartFile file);

    RoomResponse getRoomById(Long roomId);

    List<RoomResponse> getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    List<RoomResponse> getAllAvailableRooms();
}

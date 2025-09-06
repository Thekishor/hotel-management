package com.hotel_management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel_management.dto.request.RoomRequest;
import com.hotel_management.dto.response.RoomResponse;
import com.hotel_management.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @Valid @RequestPart("room") String roomInfo,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RoomRequest request = mapper.readValue(roomInfo, RoomRequest.class);
            RoomResponse roomResponse = roomService.addRoom(request, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(roomResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> responses = roomService.getAllRooms();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomsTypes() {
        List<String> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.status(HttpStatus.OK).body(roomTypes);
    }

    @GetMapping("room-by-id/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("roomId") Long roomId) {
        RoomResponse roomResponse = roomService.getRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(roomResponse);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRoom() {
        List<RoomResponse> availableRooms = roomService.getAllAvailableRooms();
        return ResponseEntity.status(HttpStatus.OK).body(availableRooms);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<?> getAvailableRoomsByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ) {
        if (checkInDate == null || checkOutDate == null || roomType.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide values for all the fields");
        }
        List<RoomResponse> availableRoomsByDateAndType =
                roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(HttpStatus.OK).body(availableRoomsByDateAndType);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestPart("room") String roomInfo,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RoomRequest request = mapper.readValue(roomInfo, RoomRequest.class);
            RoomResponse roomResponse = roomService.updateRoom(roomId, request, file);
            return ResponseEntity.status(HttpStatus.OK).body(roomResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body("Room deleted successfully");
    }

}

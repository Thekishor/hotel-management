package com.hotel_management.service;

import com.hotel_management.dto.request.RoomRequest;
import com.hotel_management.dto.response.RoomResponse;
import com.hotel_management.entity.Room;
import com.hotel_management.exception.RoomNotFoundException;
import com.hotel_management.repository.BookingRepository;
import com.hotel_management.repository.RoomRepository;
import com.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final BookingRepository bookingRepository;

    private final FileService fileService;

    @Override
    public RoomResponse addRoom(RoomRequest request, MultipartFile file) {
        try {
            String imgUrl = fileService.uploadFile(file);
            Room room = Utils.mapRoomRequestToRoomEntity(request);
            room.setImgUrl(imgUrl);
            Room savedRoom = roomRepository.save(room);
            return Utils.mapRoomEntityToRoomResponse(savedRoom);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        try {
            return roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .map(Utils::mapRoomEntityToRoomResponse).toList();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void deleteRoom(Long roomId) {
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));
            boolean isFileDeleted = fileService.deleteFile(room.getImgUrl());
            if (isFileDeleted) {
                roomRepository.delete(room);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete the image");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest request, MultipartFile file) {
        try {
            String imgUrl = null;
            if (file != null && !file.isEmpty()) {
                imgUrl = fileService.uploadFile(file);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));
            if (request.getRoomType() != null) room.setRoomType(request.getRoomType());
            if (request.getPrice() != null) room.setPrice(request.getPrice());
            if (request.getDescription() != null) room.setDescription(request.getDescription());
            if (imgUrl != null) room.setImgUrl(imgUrl);

            Room updatedRoom = roomRepository.save(room);
            return Utils.mapRoomEntityToRoomResponse(updatedRoom);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));
            return Utils.mapRoomEntityToRoomResponse(room);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<RoomResponse> getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        try {
            return roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType)
                    .stream().map(Utils::mapRoomEntityToRoomResponse).toList();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<RoomResponse> getAllAvailableRooms() {
        try {
            return roomRepository.getAllAvailableRooms()
                    .stream().map(Utils::mapRoomEntityToRoomResponse).toList();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}

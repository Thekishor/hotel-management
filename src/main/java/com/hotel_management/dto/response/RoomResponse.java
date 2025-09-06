package com.hotel_management.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RoomResponse {

    private Long id;

    private String roomType;

    private BigDecimal price;

    private String imgUrl;

    private String description;

    private List<BookingResponse> bookingResponses = new ArrayList<>();
}

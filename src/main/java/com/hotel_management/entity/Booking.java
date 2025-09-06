package com.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int numberOfAdults;

    private int numberOfChildren;

    private int totalNumberOfGuest;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @PrePersist
    @PreUpdate
    public void calculateNumberOfGuest(){
        this.totalNumberOfGuest = this.numberOfAdults + this.numberOfChildren;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", id=" + id +
                ", checkInData=" + checkInDate +
                ", checkOutData=" + checkOutDate +
                ", numberOfAdults=" + numberOfAdults +
                ", numberOfChildren=" + numberOfChildren +
                ", totalNumberOfGuest=" + totalNumberOfGuest +
                '}';
    }
}
package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class BookingUpdate {
    private String bookingId;
    private String custId;
    private String custName;
    private String roomType;
    private String roomNo;
    private String sleepCount;
    private String bookFrom;
    private String duration;
    private String cost;
}

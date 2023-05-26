package com.edengardensigiriya.edengarden.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BookingTM {
    String bookingId;
    String custId;
    String custName;
    String roomNo;
    String bookFrom;
    String duration;
    String cost;
    String bookedOn;
    String availability;
}

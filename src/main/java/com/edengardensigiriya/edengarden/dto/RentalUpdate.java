package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RentalUpdate {
    private String bookingId;
    private String custId;
    private String custName;
    private String vehicle;
    private String vehicleType;
    private String vehicleId;
    private String bookFrom;
    private String duration;
    private String cost;
}

package com.edengardensigiriya.edengarden.dto.tm;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RentalTM {
    private String rentId;
    private String custId;
    private String custName;
    private String vehicleType;
    private String vehicleId;
    private String rentFrom;
    private String rentDuration;
    private String rentCost;
    private String rentStatus;
}

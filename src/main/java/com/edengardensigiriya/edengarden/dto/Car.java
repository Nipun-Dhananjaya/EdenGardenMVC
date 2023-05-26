package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Car {
    private String regNo;
    private String brand;
    private String carType;
    private String colour;
    private String status;
}

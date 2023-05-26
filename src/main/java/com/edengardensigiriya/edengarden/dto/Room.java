package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Room {
    private String roomNo;
    private String roomType;
    private String sleepCount;
    private String costPerDay;
    private String availability;
}

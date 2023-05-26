package com.edengardensigiriya.edengarden.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RoomTM {
    private String roomNo;
    private String roomType;
    private String sleepCount;
    private String costPerDay;
    private String availability;
}

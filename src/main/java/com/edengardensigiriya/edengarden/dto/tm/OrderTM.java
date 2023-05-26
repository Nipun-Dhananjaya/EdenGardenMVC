package com.edengardensigiriya.edengarden.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderTM {
    private String ordId;
    private String suppId;
    private String items;
    private String qty;
    private String orderedDateTime;
    private String deliverDateTime;
    private String cost;
    private String status;
}

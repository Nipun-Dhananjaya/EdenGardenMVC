package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private String ordId;
    private String suppId;
    private String items;
    private String qty;
    private String orderedDateTime;
    private String deliverDateTime;
    private String cost;
    private String status;
}

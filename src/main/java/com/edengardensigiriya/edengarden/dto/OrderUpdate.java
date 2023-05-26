package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderUpdate {
    private String ordId;
    private String suppId;
    private String deleverDateTime;
    private String cost;
}

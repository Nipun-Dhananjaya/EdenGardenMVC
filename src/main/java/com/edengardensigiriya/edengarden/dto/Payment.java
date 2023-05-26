package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Payment {
    private String paymentId;
    private String custId;
    private String dateTime;
    private String reason;
    private String cost;
    private String status;
}

package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Transport {
    private String transId;
    private String custId;
    private String custName;
    private String dateTime;
    private String destination;
    private String cost;
    private String status;
}

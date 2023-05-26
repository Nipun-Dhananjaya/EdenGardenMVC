package com.edengardensigiriya.edengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TransportUpdate {
    private String transId;
    private String custId;
    private String custName;
    private String bookFrom;
    private String destination;
    private String cost;
}

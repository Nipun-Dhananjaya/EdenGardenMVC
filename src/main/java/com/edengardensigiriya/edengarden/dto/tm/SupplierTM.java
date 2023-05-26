package com.edengardensigiriya.edengarden.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SupplierTM {
    private String suppId;
    private String suppName;
    private String suppAddress;
    private String suppEmail;
    private String suppContact;
    private String ItemType;
    private String contactStartDate;
    private String contactEndDate;
}

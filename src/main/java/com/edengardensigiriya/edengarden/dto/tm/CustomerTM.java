package com.edengardensigiriya.edengarden.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CustomerTM {
    private String custId;
    private String custName;
    private String custNic;
    private String custEmail;
    private String custAddress;
    private String custContact;
    private String custGender;
}

package com.edengardensigiriya.edengarden.dto;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderItem {
    private String item;
    private String qty;
}

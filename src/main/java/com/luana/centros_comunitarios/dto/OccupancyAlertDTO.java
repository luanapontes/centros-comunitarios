package com.luana.centros_comunitarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OccupancyAlertDTO {

    private String centerName;
    private int currentOccupancy;
    private int capacity;
    private String alertMessage;
}

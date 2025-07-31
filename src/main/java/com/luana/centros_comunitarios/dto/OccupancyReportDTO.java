package com.luana.centros_comunitarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccupancyReportDTO {

    private String id;
    private String name;
    private int currentOccupancy;
    private int maxCapacity;
    private double occupancyRate; // Porcentagem de ocupação
}

package com.luana.centros_comunitarios.dto;

import com.luana.centros_comunitarios.model.Resource;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequestDTO {

    private String fromCenterId;  // Centro que oferece recursos
    private String toCenterId;    // Centro que recebe recursos

    private List<Resource> resourcesGiven;    // Recursos que saem do fromCenter
    private List<Resource> resourcesReceived; // Recursos que entram no fromCenter
}

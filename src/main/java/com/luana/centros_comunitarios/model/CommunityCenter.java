package com.luana.centros_comunitarios.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "community_centers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCenter {

    @Id
    private String id;

    private String name;
    private String address;
    private String location;
    private int maxCapacity; // Quantidade máxima de pessoas suportadas
    private int currentOccupancy; // Quantidade atual de pessoas ocupando o centro

    private List<Resource> resources = new ArrayList<>(); // Lista dos recursos disponíveis
}

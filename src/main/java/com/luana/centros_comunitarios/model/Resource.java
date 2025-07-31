package com.luana.centros_comunitarios.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "resources")
public class Resource {

    @Id
    private String id;

    private ResourceType type; // Tipo do recurso
    private int quantity;
}

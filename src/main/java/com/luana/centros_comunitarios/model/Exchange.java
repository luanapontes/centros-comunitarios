package com.luana.centros_comunitarios.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "exchanges")
public class Exchange {

    private String id;

    private String centerFromId;   // Centro que está oferecendo recursos
    private String centerToId;     // Centro que está recebendo recursos

    private List<Resource> resourcesFrom; // Recursos que saem do centerFrom
    private List<Resource> resourcesTo;   // Recursos que saem do centerTo

    private LocalDateTime timestamp;      // Data e hora da troca
}

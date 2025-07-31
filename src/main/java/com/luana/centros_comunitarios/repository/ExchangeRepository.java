package com.luana.centros_comunitarios.repository;

import com.luana.centros_comunitarios.model.Exchange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeRepository extends MongoRepository<Exchange, String> {

    // Buscar histórico de um centro a partir de uma data
    List<Exchange> findByCenterFromIdOrCenterToIdAndTimestampAfter(
            String centerFromId, String centerToId, LocalDateTime dateFrom);
}

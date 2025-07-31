package com.luana.centros_comunitarios.repository;

import com.luana.centros_comunitarios.model.CommunityCenter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCenterRepository extends MongoRepository<CommunityCenter, String> {
}

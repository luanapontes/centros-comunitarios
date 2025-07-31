package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;

    public List<Resource> listAll() {
        return repository.findAll();
    }

    public Resource findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    public Resource create(Resource resource) {
        return repository.save(resource);
    }

    public Resource update(String id, Resource updatedResource) {
        Resource existing = findById(id);

        existing.setType(updatedResource.getType());
        existing.setQuantity(updatedResource.getQuantity());

        return repository.save(existing);
    }

    public void delete(String id) {
        Resource existing = findById(id);
        repository.delete(existing);
    }
}

package com.luana.centros_comunitarios.controller;

import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recursos", description = "Gerenciamento de recursos dos centros")
@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @Operation(summary = "Listar todos os recursos")
    @GetMapping
    public List<Resource> listAll() {
        return service.listAll();
    }

    @Operation(summary = "Buscar recurso por ID")
    @GetMapping("/{id}")
    public Resource findById(@PathVariable String id) {
        return service.findById(id);
    }

    @Operation(summary = "Criar um novo recurso")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(@RequestBody Resource resource) {
        return service.create(resource);
    }

    @Operation(summary = "Atualizar um recurso existente")
    @PutMapping("/{id}")
    public Resource update(@PathVariable String id, @RequestBody Resource updatedResource) {
        return service.update(id, updatedResource);
    }

    @Operation(summary = "Deletar um recurso por ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

}

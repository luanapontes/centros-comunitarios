package com.luana.centros_comunitarios.controller;

import com.luana.centros_comunitarios.dto.OccupancyAlertDTO;
import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.service.CommunityCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Tag(name = "Centros Comunitários", description = "Gerenciamento dos centros comunitários e suas ocupações")
@RestController
@RequestMapping("/centers")
@RequiredArgsConstructor
public class CommunityCenterController {

    private final CommunityCenterService service;

    @Operation(summary = "Listar todos os centros comunitários")
    @GetMapping
    public List<CommunityCenter> listAll() {
        return service.listAll();
    }

    @Operation(summary = "Buscar centro comunitário por ID")
    @GetMapping("/{id}")
    public CommunityCenter findById(@PathVariable String id) {
        return service.findById(id);
    }

    @Operation(summary = "Criar um novo centro comunitário")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityCenter create(@RequestBody CommunityCenter center) {
        return service.save(center);
    }

    @Operation(summary = "Atualizar um centro comunitário existente")
    @PutMapping("/{id}")
    public CommunityCenter update(@PathVariable String id, @RequestBody CommunityCenter updatedCenter) {
        return service.update(id, updatedCenter);
    }

    @Operation(summary = "Deletar um centro comunitário por ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }

    @Operation(summary = "Atualizar a ocupação atual de um centro comunitário")
    @PatchMapping("/{id}/occupancy")
    public CommunityCenter updateOccupancy(@PathVariable String id, @RequestBody Map<String, Integer> payload) {
        Integer newOccupancy = payload.get("currentOccupancy");
        if (newOccupancy == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo 'currentOccupancy' é obrigatório.");
        }
        return service.updateOccupancy(id, newOccupancy);
    }

    @Operation(summary = "Gerar relatório de ocupação de todos os centros")
    @GetMapping("/report/occupancy")
    public List<OccupancyReportDTO> getOccupancyReport() {
        return service.getOccupancyReport();
    }

    @Operation(summary = "Obter alertas de alta ocupação")
    @GetMapping("/notifications/occupancy-alert")
    public List<OccupancyAlertDTO> getOccupancyAlerts() {
        return service.getHighOccupancyAlerts();
    }

    @Operation(summary = "Atualizar recursos disponíveis em um centro")
    @PutMapping("/{id}/resources")
    public CommunityCenter updateResources(@PathVariable String id,
                                           @RequestBody List<Resource> newResources) {
        return service.updateResources(id, newResources);
    }
}

package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.dto.OccupancyAlertDTO;
import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.repository.CommunityCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityCenterService {

    private final CommunityCenterRepository repository;

    public List<CommunityCenter> listAll() {
        return repository.findAll();
    }

    public CommunityCenter findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro comunitário não encontrado"));
    }

    public CommunityCenter save(CommunityCenter center) {
        return repository.save(center);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public CommunityCenter update(String id, CommunityCenter updatedCenter) {
        Optional<CommunityCenter> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Centro comunitário não encontrado");
        }

        CommunityCenter center = existing.get();
        center.setName(updatedCenter.getName());
        center.setAddress(updatedCenter.getAddress());
        center.setLocation(updatedCenter.getLocation());
        center.setMaxCapacity(updatedCenter.getMaxCapacity());
        center.setCurrentOccupancy(updatedCenter.getCurrentOccupancy());
        center.setResources(updatedCenter.getResources());

        return repository.save(center);
    }

    public CommunityCenter updateOccupancy(String id, int newOccupancy) {
        CommunityCenter center = findById(id);

        center.setCurrentOccupancy(newOccupancy);

        CommunityCenter updatedCenter = repository.save(center);

        if (newOccupancy >= center.getMaxCapacity()) {
            notifyCapacityReached(updatedCenter);
        }

        return updatedCenter;
    }

    private void notifyCapacityReached(CommunityCenter center) {
        System.out.println("Notificação: Centro " + center.getName() + " atingiu capacidade máxima!");
    }

    public List<OccupancyReportDTO> getOccupancyReport() {
        return repository.findAll().stream()
                .map(center -> OccupancyReportDTO.builder()
                        .id(center.getId())
                        .name(center.getName())
                        .currentOccupancy(center.getCurrentOccupancy())
                        .maxCapacity(center.getMaxCapacity())
                        .occupancyRate(
                                center.getMaxCapacity() == 0 ? 0 :
                                        (double) center.getCurrentOccupancy() / center.getMaxCapacity() * 100
                        )
                        .build())
                .collect(Collectors.toList());
    }

    public List<OccupancyAlertDTO> getHighOccupancyAlerts() {
        return repository.findAll().stream()
                .filter(center -> center.getMaxCapacity() > 0)
                .filter(center -> ((double) center.getCurrentOccupancy() / center.getMaxCapacity()) > 0.9)
                .map(center -> new OccupancyAlertDTO(
                        center.getName(),
                        center.getCurrentOccupancy(),
                        center.getMaxCapacity(),
                        "Ocupação acima de 90%"))
                .toList();
    }

    public CommunityCenter updateResources(String id, List<Resource> newResources) {
        CommunityCenter center = findById(id);
        center.setResources(newResources);
        return repository.save(center);
    }
}

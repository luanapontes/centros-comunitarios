package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.repository.CommunityCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CommunityCenterRepository communityCenterRepository;

    public List<OccupancyReportDTO> generateOccupancyReport() {
        List<CommunityCenter> centers = communityCenterRepository.findAll();

        return centers.stream().map(center -> {
            double occupancyRate = calculateOccupancyRate(
                    center.getCurrentOccupancy(),
                    center.getMaxCapacity()
            );

            return OccupancyReportDTO.builder()
                    .id(center.getId())
                    .name(center.getName())
                    .currentOccupancy(center.getCurrentOccupancy())
                    .maxCapacity(center.getMaxCapacity())
                    .occupancyRate(occupancyRate)
                    .build();
        }).collect(Collectors.toList());
    }

    private double calculateOccupancyRate(int current, int max) {
        if (max == 0) return 0.0;
        return (double) current / max * 100;
    }
}

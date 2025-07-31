package com.luana.centros_comunitarios.controller;

import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Relatórios", description = "Relatórios de ocupação dos centros")
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Relatório de ocupação dos centros")
    @GetMapping("/occupancy")
    public List<OccupancyReportDTO> getOccupancyReport() {
        return reportService.generateOccupancyReport();
    }
}

package com.luana.centros_comunitarios.controller;

import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void deveRetornarRelatorioDeOcupacao() throws Exception {
        OccupancyReportDTO report = OccupancyReportDTO.builder()
                .id("1")
                .name("Centro Teste")
                .currentOccupancy(45)
                .maxCapacity(100)
                .occupancyRate(45.0)
                .build();

        when(reportService.generateOccupancyReport()).thenReturn(List.of(report));

        mockMvc.perform(get("/reports/occupancy")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Centro Teste"))
                .andExpect(jsonPath("$[0].currentOccupancy").value(45))
                .andExpect(jsonPath("$[0].maxCapacity").value(100))
                .andExpect(jsonPath("$[0].occupancyRate").value(45.0));
    }
}

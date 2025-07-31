package com.luana.centros_comunitarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luana.centros_comunitarios.dto.OccupancyAlertDTO;
import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.service.CommunityCenterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityCenterController.class)
class CommunityCenterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityCenterService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CommunityCenter exampleCenter;
    private OccupancyAlertDTO alert;

    @BeforeEach
    void setup() {
        exampleCenter = CommunityCenter.builder()
                .id("1")
                .name("Centro Exemplo")
                .build();

        alert = OccupancyAlertDTO.builder()
                .centerName("Centro X")
                .currentOccupancy(90)
                .capacity(100)
                .alertMessage("Alta ocupação")
                .build();
    }

    @Test
    void deveRetornarListaDeCentros() throws Exception {
        when(service.listAll()).thenReturn(List.of(exampleCenter));

        mockMvc.perform(get("/centers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Centro Exemplo"));

        verify(service).listAll();
    }

    @Test
    void deveCriarCentroComStatus201() throws Exception {
        when(service.save(any(CommunityCenter.class))).thenReturn(exampleCenter);

        mockMvc.perform(post("/centers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleCenter)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));

        verify(service).save(any(CommunityCenter.class));
    }

    @Test
    void deveAtualizarOcupacao() throws Exception {
        Map<String, Integer> payload = Map.of("currentOccupancy", 50);
        exampleCenter.setCurrentOccupancy(50);

        when(service.updateOccupancy(eq("1"), eq(50))).thenReturn(exampleCenter);

        mockMvc.perform(patch("/centers/1/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentOccupancy").value(50));

        verify(service).updateOccupancy("1", 50);
    }

    @Test
    void deveRetornarBadRequestSeOcupacaoNaoInformada() throws Exception {
        mockMvc.perform(patch("/centers/1/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarRelatorioDeOcupacao() throws Exception {
        OccupancyReportDTO report = OccupancyReportDTO.builder()
                .id("1").name("Centro Exemplo").currentOccupancy(50).maxCapacity(100).occupancyRate(50.0)
                .build();

        when(service.getOccupancyReport()).thenReturn(List.of(report));

        mockMvc.perform(get("/centers/report/occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].occupancyRate").value(50.0));

        verify(service).getOccupancyReport();
    }

    @Test
    public void deveRetornarAlertasDeOcupacao() throws Exception {
        when(service.getHighOccupancyAlerts()).thenReturn(List.of(alert));

        mockMvc.perform(get("/centers/notifications/occupancy-alert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].centerName").value("Centro X"))
                .andExpect(jsonPath("$[0].currentOccupancy").value(90))
                .andExpect(jsonPath("$[0].capacity").value(100))
                .andExpect(jsonPath("$[0].alertMessage").value("Alta ocupação"));
    }

    @Test
    void deveAtualizarRecursos() throws Exception {
        List<Resource> resources = List.of(Resource.builder().type(null).quantity(10).build());
        when(service.updateResources(eq("1"), anyList())).thenReturn(exampleCenter);

        mockMvc.perform(put("/centers/1/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resources)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        verify(service).updateResources(eq("1"), anyList());
    }
}

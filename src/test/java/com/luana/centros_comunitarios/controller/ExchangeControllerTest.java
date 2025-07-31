package com.luana.centros_comunitarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luana.centros_comunitarios.dto.ExchangeRequestDTO;
import com.luana.centros_comunitarios.model.Exchange;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.model.ResourceType;
import com.luana.centros_comunitarios.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeController.class)
public class ExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeService exchangeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveFazerTrocaERetornar201() throws Exception {
        ExchangeRequestDTO request = new ExchangeRequestDTO();
        request.setFromCenterId("center1");
        request.setToCenterId("center2");
        request.setResourcesGiven(List.of(new Resource(null, ResourceType.CESTA_BASICA, 5)));
        request.setResourcesReceived(List.of(new Resource(null, ResourceType.MEDICO, 1)));

        Exchange exchange = Exchange.builder()
                .centerFromId("center1")
                .centerToId("center2")
                .resourcesFrom(request.getResourcesGiven())
                .resourcesTo(request.getResourcesReceived())
                .timestamp(LocalDateTime.now())
                .build();

        when(exchangeService.exchangeResources(
                anyString(),
                anyString(),
                anyList(),
                anyList()
        )).thenReturn(exchange);

        mockMvc.perform(post("/exchanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.centerFromId").value("center1"))
                .andExpect(jsonPath("$.centerToId").value("center2"))
                .andExpect(jsonPath("$.resourcesFrom[0].type").value("CESTA_BASICA"))
                .andExpect(jsonPath("$.resourcesTo[0].type").value("MEDICO"));
    }

    @Test
    void deveRetornarHistoricoDeTrocasComData() throws Exception {
        Exchange exchange = Exchange.builder()
                .centerFromId("center1")
                .centerToId("center2")
                .resourcesFrom(List.of(new Resource(null, ResourceType.VOLUNTARIO, 3)))
                .resourcesTo(List.of(new Resource(null, ResourceType.VEICULO, 1)))
                .timestamp(LocalDateTime.now())
                .build();

        when(exchangeService.getHistory(eq("center1"), any(LocalDateTime.class)))
                .thenReturn(List.of(exchange));

        mockMvc.perform(get("/exchanges/history")
                        .param("centerId", "center1")
                        .param("from", "2023-01-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].centerFromId").value("center1"))
                .andExpect(jsonPath("$[0].resourcesFrom[0].type").value("VOLUNTARIO"));
    }

    @Test
    void deveRetornarHistoricoDeTrocasSemData() throws Exception {
        Exchange exchange = Exchange.builder()
                .centerFromId("center1")
                .centerToId("center2")
                .resourcesFrom(List.of(new Resource(null, ResourceType.VOLUNTARIO, 3)))
                .resourcesTo(List.of(new Resource(null, ResourceType.VEICULO, 1)))
                .timestamp(LocalDateTime.now())
                .build();

        when(exchangeService.getHistory(eq("center1"), any(LocalDateTime.class)))
                .thenReturn(List.of(exchange));

        // Sem param 'from', deve usar valor default do controller
        mockMvc.perform(get("/exchanges/history")
                        .param("centerId", "center1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].centerFromId").value("center1"))
                .andExpect(jsonPath("$[0].resourcesFrom[0].type").value("VOLUNTARIO"));
    }
}

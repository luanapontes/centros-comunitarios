package com.luana.centros_comunitarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.model.ResourceType;
import com.luana.centros_comunitarios.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Resource exampleResource;

    @BeforeEach
    void setup() {
        exampleResource = Resource.builder()
                .id("1")
                .type(ResourceType.CESTA_BASICA)
                .quantity(10)
                .build();
    }

    @Test
    void deveListarTodosResources() throws Exception {
        when(service.listAll()).thenReturn(List.of(exampleResource));

        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].type").value("CESTA_BASICA"))
                .andExpect(jsonPath("$[0].quantity").value(10));

        verify(service).listAll();
    }

    @Test
    void deveBuscarResourcePorId() throws Exception {
        when(service.findById("1")).thenReturn(exampleResource);

        mockMvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.type").value("CESTA_BASICA"))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(service).findById("1");
    }

    @Test
    void deveCriarResourceComStatus201() throws Exception {
        when(service.create(any(Resource.class))).thenReturn(exampleResource);

        mockMvc.perform(post("/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleResource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));

        verify(service).create(any(Resource.class));
    }

    @Test
    void deveAtualizarResource() throws Exception {
        Resource updated = Resource.builder()
                .id("1")
                .type(ResourceType.VEICULO)
                .quantity(20)
                .build();

        when(service.update(eq("1"), any(Resource.class))).thenReturn(updated);

        mockMvc.perform(put("/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.type").value("VEICULO"))
                .andExpect(jsonPath("$.quantity").value(20));

        verify(service).update(eq("1"), any(Resource.class));
    }

    @Test
    void deveDeletarResourceComStatus204() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/resources/1"))
                .andExpect(status().isNoContent());

        verify(service).delete("1");
    }
}

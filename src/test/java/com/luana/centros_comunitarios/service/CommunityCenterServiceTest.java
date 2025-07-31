package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.dto.OccupancyAlertDTO;
import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.repository.CommunityCenterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityCenterServiceTest {

    @Mock
    private CommunityCenterRepository repository;

    @InjectMocks
    private CommunityCenterService service;

    private AutoCloseable closeable;

    private CommunityCenter mockCenter;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockCenter = CommunityCenter.builder()
                .id("1")
                .name("Centro A")
                .address("Rua X")
                .location("Cidade Y")
                .maxCapacity(100)
                .currentOccupancy(91)
                .resources(List.of(new Resource()))
                .build();
    }

    @Test
    void testFindById_found() {
        when(repository.findById("1")).thenReturn(Optional.of(mockCenter));

        CommunityCenter result = service.findById("1");

        assertEquals("Centro A", result.getName());
    }

    @Test
    void testFindById_notFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById("999"));
    }

    @Test
    void testUpdateOccupancy_triggersNotification() {
        when(repository.findById("1")).thenReturn(Optional.of(mockCenter));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CommunityCenter updated = service.updateOccupancy("1", 100);

        assertEquals(100, updated.getCurrentOccupancy());
    }

    @Test
    void testGetOccupancyReport() {
        when(repository.findAll()).thenReturn(List.of(mockCenter));

        List<OccupancyReportDTO> reports = service.getOccupancyReport();

        assertEquals(1, reports.size());
        assertEquals("Centro A", reports.get(0).getName());
        assertEquals(91.0, reports.get(0).getOccupancyRate());
    }

    @Test
    void testGetHighOccupancyAlerts() {
        when(repository.findAll()).thenReturn(List.of(mockCenter));

        List<OccupancyAlertDTO> alerts = service.getHighOccupancyAlerts();

        assertEquals(1, alerts.size());
        assertEquals("Centro A", alerts.get(0).getCenterName());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}

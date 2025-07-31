package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.dto.OccupancyReportDTO;
import com.luana.centros_comunitarios.model.CommunityCenter;
import com.luana.centros_comunitarios.repository.CommunityCenterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private CommunityCenterRepository communityCenterRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveGerarRelatorioComTaxaDeOcupacaoCorreta() {
        CommunityCenter c1 = new CommunityCenter();
        c1.setId("c1");
        c1.setName("Centro 1");
        c1.setCurrentOccupancy(45);
        c1.setMaxCapacity(100);

        CommunityCenter c2 = new CommunityCenter();
        c2.setId("c2");
        c2.setName("Centro 2");
        c2.setCurrentOccupancy(0);
        c2.setMaxCapacity(0); // para testar divisão por zero

        when(communityCenterRepository.findAll()).thenReturn(List.of(c1, c2));

        List<OccupancyReportDTO> report = reportService.generateOccupancyReport();

        assertEquals(2, report.size());

        OccupancyReportDTO dto1 = report.stream().filter(r -> r.getId().equals("c1")).findFirst().orElse(null);
        OccupancyReportDTO dto2 = report.stream().filter(r -> r.getId().equals("c2")).findFirst().orElse(null);

        assertNotNull(dto1);
        assertEquals("Centro 1", dto1.getName());
        assertEquals(45, dto1.getCurrentOccupancy());
        assertEquals(100, dto1.getMaxCapacity());
        assertEquals(45.0, dto1.getOccupancyRate(), 0.001);

        assertNotNull(dto2);
        assertEquals("Centro 2", dto2.getName());
        assertEquals(0, dto2.getCurrentOccupancy());
        assertEquals(0, dto2.getMaxCapacity());
        assertEquals(0.0, dto2.getOccupancyRate(), 0.001);

        verify(communityCenterRepository, times(1)).findAll();
    }
}

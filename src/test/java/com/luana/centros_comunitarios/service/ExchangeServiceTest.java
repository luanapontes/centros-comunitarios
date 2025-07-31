package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.model.*;
import com.luana.centros_comunitarios.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private CommunityCenterService communityCenterService;

    @InjectMocks
    private ExchangeService exchangeService;

    private CommunityCenter centerFrom;
    private CommunityCenter centerTo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Criar centros de comunidade com recursos e capacidades
        centerFrom = new CommunityCenter();
        centerFrom.setId("from");
        centerFrom.setMaxCapacity(100);
        centerFrom.setCurrentOccupancy(50);
        centerFrom.setResources(new ArrayList<>());

        centerTo = new CommunityCenter();
        centerTo.setId("to");
        centerTo.setMaxCapacity(100);
        centerTo.setCurrentOccupancy(50);
        centerTo.setResources(new ArrayList<>());

        // Adicionar recursos para centerFrom
        Resource r1 = new Resource();
        r1.setType(ResourceType.CESTA_BASICA);
        r1.setQuantity(5);
        centerFrom.getResources().add(r1);

        // Adicionar recursos para centerTo
        Resource r2 = new Resource();
        r2.setType(ResourceType.MEDICO);
        r2.setQuantity(5);
        centerTo.getResources().add(r2);
    }

    @Test
    void deveRealizarTrocaComPontosIguais() {
        // Preparar recursos oferecidos e recebidos com pontos iguais
        List<Resource> resourcesFrom = new ArrayList<>();
        Resource fromResource = new Resource();
        fromResource.setType(ResourceType.CESTA_BASICA); // pontos 2
        fromResource.setQuantity(2); // total 4 pontos
        resourcesFrom.add(fromResource);

        List<Resource> resourcesTo = new ArrayList<>();
        Resource toResource = new Resource();
        toResource.setType(ResourceType.MEDICO); // pontos 4
        toResource.setQuantity(1); // total 4 pontos
        resourcesTo.add(toResource);

        when(communityCenterService.findById("from")).thenReturn(centerFrom);
        when(communityCenterService.findById("to")).thenReturn(centerTo);
        when(exchangeRepository.save(any(Exchange.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exchange result = exchangeService.exchangeResources("from", "to", resourcesFrom, resourcesTo);

        assertNotNull(result);
        assertEquals("from", result.getCenterFromId());
        assertEquals("to", result.getCenterToId());

        // Verifica se os recursos foram atualizados corretamente
        assertEquals(3, centerFrom.getResources().stream()
                .filter(r -> r.getType() == ResourceType.CESTA_BASICA)
                .findFirst()
                .get()
                .getQuantity());

        assertEquals(4, centerTo.getResources().stream()
                .filter(r -> r.getType() == ResourceType.MEDICO)
                .findFirst()
                .get()
                .getQuantity());

        verify(communityCenterService, times(1)).save(centerFrom);
        verify(communityCenterService, times(1)).save(centerTo);
        verify(exchangeRepository, times(1)).save(any(Exchange.class));
    }

    @Test
    void deveLancarExcecaoQuandoPontosDiferentesSemAltaOcupacao() {
        // resourcesFrom 4 pontos, resourcesTo 7 pontos (KIT_MEDICO)
        List<Resource> resourcesFrom = new ArrayList<>();
        Resource fromResource = new Resource();
        fromResource.setType(ResourceType.CESTA_BASICA); // 2 pontos
        fromResource.setQuantity(2); // 4 pontos
        resourcesFrom.add(fromResource);

        List<Resource> resourcesTo = new ArrayList<>();
        Resource toResource = new Resource();
        toResource.setType(ResourceType.KIT_MEDICO); // 7 pontos
        toResource.setQuantity(1); // 7 pontos
        resourcesTo.add(toResource);

        when(communityCenterService.findById("from")).thenReturn(centerFrom);
        when(communityCenterService.findById("to")).thenReturn(centerTo);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeService.exchangeResources("from", "to", resourcesFrom, resourcesTo);
        });

        assertEquals("400 BAD_REQUEST \"Pontos dos recursos oferecidos devem ser iguais, exceto se centro com ocupação > 90%\"", exception.getMessage());
    }

    @Test
    void naoDeveLancarExcecaoQuandoPontosDiferentesComAltaOcupacao() {
        centerFrom.setCurrentOccupancy(95); // > 90% do maxCapacity

        // Recursos oferecidos por "from"
        List<Resource> resourcesFrom = new ArrayList<>();
        Resource fromResource = new Resource();
        fromResource.setType(ResourceType.CESTA_BASICA);
        fromResource.setQuantity(2);
        resourcesFrom.add(fromResource);

        // Recursos oferecidos por "to"
        List<Resource> resourcesTo = new ArrayList<>();
        Resource toResource = new Resource();
        toResource.setType(ResourceType.KIT_MEDICO);
        toResource.setQuantity(1);
        resourcesTo.add(toResource);

        // Agora adiciona o recurso KIT_MEDICO ao centerTo para satisfazer a validação
        Resource kitMedico = new Resource();
        kitMedico.setType(ResourceType.KIT_MEDICO);
        kitMedico.setQuantity(5);  // quantidade suficiente
        centerTo.getResources().add(kitMedico);

        when(communityCenterService.findById("from")).thenReturn(centerFrom);
        when(communityCenterService.findById("to")).thenReturn(centerTo);
        when(exchangeRepository.save(any(Exchange.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> {
            exchangeService.exchangeResources("from", "to", resourcesFrom, resourcesTo);
        });
    }
}

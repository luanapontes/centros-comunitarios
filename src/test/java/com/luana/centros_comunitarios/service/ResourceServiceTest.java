package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.model.Resource;
import com.luana.centros_comunitarios.model.ResourceType;
import com.luana.centros_comunitarios.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    private Resource resource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resource = new Resource();
        resource.setId("1");
        resource.setType(ResourceType.CESTA_BASICA);
        resource.setQuantity(10);
    }

    @Test
    void deveListarTodosOsRecursos() {
        List<Resource> resources = List.of(resource);
        when(resourceRepository.findAll()).thenReturn(resources);

        List<Resource> resultado = resourceService.listAll();

        assertEquals(1, resultado.size());
        verify(resourceRepository, times(1)).findAll();
    }

    @Test
    void deveEncontrarRecursoPorId() {
        when(resourceRepository.findById("1")).thenReturn(Optional.of(resource));

        Resource resultado = resourceService.findById("1");

        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        verify(resourceRepository, times(1)).findById("1");
    }

    @Test
    void deveLancarExceptionQuandoRecursoNaoEncontrado() {
        when(resourceRepository.findById("2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resourceService.findById("2");
        });

        assertEquals("404 NOT_FOUND \"Resource not found\"", exception.getMessage());
    }

    @Test
    void deveCriarRecurso() {
        when(resourceRepository.save(resource)).thenReturn(resource);

        Resource resultado = resourceService.create(resource);

        assertNotNull(resultado);
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    void deveAtualizarRecurso() {
        Resource updated = new Resource();
        updated.setType(ResourceType.VEICULO);
        updated.setQuantity(5);

        when(resourceRepository.findById("1")).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Resource resultado = resourceService.update("1", updated);

        assertEquals(ResourceType.VEICULO, resultado.getType());
        assertEquals(5, resultado.getQuantity());
        verify(resourceRepository).findById("1");
        verify(resourceRepository).save(resource);
    }

    @Test
    void deveDeletarRecurso() {
        when(resourceRepository.findById("1")).thenReturn(Optional.of(resource));
        doNothing().when(resourceRepository).delete(resource);

        assertDoesNotThrow(() -> resourceService.delete("1"));

        verify(resourceRepository).findById("1");
        verify(resourceRepository).delete(resource);
    }

    @Test
    void deveLancarExceptionAoDeletarRecursoInexistente() {
        when(resourceRepository.findById("99")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resourceService.delete("99");
        });

        assertEquals("404 NOT_FOUND \"Resource not found\"", exception.getMessage());
        verify(resourceRepository, never()).delete(any());
    }
}

package com.perfulandia.productservice.service;

import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deberiaRetornarTodosLosProductos() {
        Producto p1 = Producto.builder().id(1L).nombre("A").precio(10.0).stock(5).build();
        Producto p2 = Producto.builder().id(2L).nombre("B").precio(20.0).stock(3).build();
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(p1) && resultado.contains(p2));
        verify(repo, times(1)).findAll();
    }

    @Test
    void guardar_deberiaPersistirProducto() {
        Producto input = Producto.builder().nombre("C").precio(30.0).stock(2).build();
        Producto saved = Producto.builder().id(3L).nombre("C").precio(30.0).stock(2).build();
        when(repo.save(input)).thenReturn(saved);

        Producto resultado = service.guardar(input);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        verify(repo, times(1)).save(input);
    }

    @Test
    void bucarPorId_cuandoExiste_deberiaRetornarProducto() {
        Producto esperado = Producto.builder().id(4L).nombre("D").precio(40.0).stock(1).build();
        when(repo.findById(4L)).thenReturn(Optional.of(esperado));

        Producto resultado = service.bucarPorId(4L);

        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        verify(repo, times(1)).findById(4L);
    }

    @Test
    void bucarPorId_cuandoNoExiste_deberiaRetornarNull() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.bucarPorId(99L);

        assertNull(resultado);
        verify(repo, times(1)).findById(99L);
    }

    @Test
    void eliminar_deberiaInvocarDeleteById() {
        service.eliminar(5L);

        verify(repo, times(1)).deleteById(5L);
    }
}


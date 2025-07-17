package com.perfulandia.usuarioservice.service;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
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

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deberiaRetornarTodosLosUsuarios() {
        Usuario u1 = new Usuario(1L, "Alice", "alice@example.com", "USER");
        Usuario u2 = new Usuario(2L, "Bob", "bob@example.com", "ADMIN");
        when(repo.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<Usuario> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(u1) && resultado.contains(u2));
        verify(repo, times(1)).findAll();
    }

    @Test
    void guardar_deberiaPersistirYRetornarUsuario() {
        Usuario input = new Usuario(0L, "Carol", "carol@example.com", "USER");
        Usuario saved = new Usuario(3L, "Carol", "carol@example.com", "USER");
        when(repo.save(input)).thenReturn(saved);

        Usuario resultado = service.guardar(input);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        verify(repo, times(1)).save(input);
    }

    @Test
    void buscar_cuandoExiste_deberiaRetornarUsuario() {
        Usuario esperado = new Usuario(4L, "Dave", "dave@example.com", "USER");
        when(repo.findById(4L)).thenReturn(Optional.of(esperado));

        Usuario resultado = service.buscar(4L);

        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        verify(repo, times(1)).findById(4L);
    }

    @Test
    void buscar_cuandoNoExiste_deberiaRetornarNull() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Usuario resultado = service.buscar(99L);

        assertNull(resultado);
        verify(repo, times(1)).findById(99L);
    }

    @Test
    void eliminar_deberiaInvocarDeleteById() {
        service.eliminar(5L);

        verify(repo, times(1)).deleteById(5L);
    }
}

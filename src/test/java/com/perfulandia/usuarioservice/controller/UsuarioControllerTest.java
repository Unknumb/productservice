package com.perfulandia.usuarioservice.controller;

import com.perfulandia.usuarioservice.model.Carrito;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService service;

    @MockBean
    private RestTemplate restTemplate;

    private Usuario sample;

    @BeforeEach
    void setUp() {
        sample = new Usuario(1L, "Alice", "alice@example.com", "USER");
    }

    @Test
    void GET_api_usuarios_deberiaRetornarLista() throws Exception {
        when(service.listar()).thenReturn(Collections.singletonList(sample));

        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Alice"));

        verify(service, times(1)).listar();
    }

    @Test
    void POST_api_usuarios_deberiaGuardar() throws Exception {
        when(service.guardar(any(Usuario.class))).thenReturn(sample);

        String body = "{"
                + "\"nombre\":\"Alice\","
                + "\"correo\":\"alice@example.com\","
                + "\"rol\":\"USER\""
                + "}";

        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(service, times(1)).guardar(captor.capture());
        assert captor.getValue().getCorreo().equals("alice@example.com");
    }

    @Test
    void GET_api_usuarios_id_deberiaRetornarEntidad() throws Exception {
        when(service.buscar(1L)).thenReturn(sample);

        mvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("USER"));

        verify(service, times(1)).buscar(1L);
    }

    @Test
    void DELETE_api_usuarios_id_deberiaEliminar() throws Exception {
        mvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).eliminar(1L);
    }

    @Test
    void GET_api_usuarios_carrito_deberiaRetornarCarrito() throws Exception {
        Carrito fakeCar = new Carrito();
        fakeCar.setId(2L);
        fakeCar.setUsuarioId(1L);
        when(restTemplate.getForObject("http://localhost:8083/api/carritos/2", Carrito.class))
                .thenReturn(fakeCar);

        mvc.perform(get("/api/usuarios/carrito/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8083/api/carritos/2", Carrito.class);
    }
}

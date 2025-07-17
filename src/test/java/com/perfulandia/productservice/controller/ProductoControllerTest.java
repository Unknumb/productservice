package com.perfulandia.productservice.controller;

import com.perfulandia.productservice.model.Carrito;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductoService service;

    @MockBean
    private RestTemplate restTemplate;

    private Producto sample;

    @BeforeEach
    void setUp() {
        sample = Producto.builder()
                .id(1L)
                .nombre("Test")
                .precio(9.99)
                .stock(100)
                .build();
    }

    @Test
    void GET_api_productos_deberiaRetornarLista() throws Exception {
        when(service.listar()).thenReturn(Collections.singletonList(sample));

        mvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Test"));

        verify(service, times(1)).listar();
    }

    @Test
    void POST_api_productos_deberiaGuardar() throws Exception {
        when(service.guardar(any(Producto.class))).thenReturn(sample);

        String body = "{"
                + "\"nombre\":\"Test\","
                + "\"precio\":9.99,"
                + "\"stock\":100"
                + "}";

        mvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(service, times(1)).guardar(captor.capture());
        assert captor.getValue().getNombre().equals("Test");
    }

    @Test
    void GET_api_productos_id_deberiaRetornarEntidad() throws Exception {
        when(service.bucarPorId(1L)).thenReturn(sample);

        mvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(9.99));

        verify(service, times(1)).bucarPorId(1L);
    }

    @Test
    void DELETE_api_productos_id_deberiaEliminar() throws Exception {
        mvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).eliminar(1L);
    }

    @Test
    void GET_api_productos_usuario_deberiaRetornarUsuario() throws Exception {
        Usuario fakeUser = new Usuario(2L, "Alice", "a@b.com", "USER");
        when(restTemplate.getForObject("http://localhost:8081/api/usuarios/2", Usuario.class))
                .thenReturn(fakeUser);

        mvc.perform(get("/api/productos/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Alice"));

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8081/api/usuarios/2", Usuario.class);
    }

    @Test
    void GET_api_productos_carrito_deberiaRetornarCarrito() throws Exception {
        Carrito fakeCar = new Carrito();
        fakeCar.setId(3L);
        fakeCar.setUsuarioId(2L);
        when(restTemplate.getForObject("http://localhost:8083/api/carritos/3", Carrito.class))
                .thenReturn(fakeCar);

        mvc.perform(get("/api/productos/carrito/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8083/api/carritos/3", Carrito.class);
    }
}

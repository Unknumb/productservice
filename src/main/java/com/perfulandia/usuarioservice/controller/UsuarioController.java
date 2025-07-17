package com.perfulandia.usuarioservice.controller;

import com.perfulandia.usuarioservice.model.Carrito;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import com.perfulandia.usuarioservice.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final RestTemplate restTemplate;
    //Constructor para poder consumir la interfaz
    public UsuarioController(UsuarioService service, RestTemplate restTemplate) {
        this.service=service;
        this.restTemplate= restTemplate;
    }

    @GetMapping
    public List<Usuario> listar(){
        return service.listar();
    }

    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario){
        return service.guardar(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable long id){
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id){
        service.eliminar(id);
    }

    //Nuevo método
    @GetMapping("/carrito/{id}")
    public Carrito obtenerCarrito(@PathVariable long id){
        return restTemplate.getForObject("http://localhost:8083/api/carritos/"+id, Carrito.class);
    }



}

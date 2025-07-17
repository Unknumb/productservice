package com.perfulandia.productservice.model;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.*;

import java.util.List;

//dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    private long id;
    private long usuarioId;
    private List<Long> productoIds;
    private Double total;

}

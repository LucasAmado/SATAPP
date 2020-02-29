package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventariable {

    private String id, codigo, tipo, nombre, descripcion, ubicacion, createdAt, updatedAt, imagen;

    public Inventariable(String codigo, String tipo, String nombre, String descripcion, String ubicacion) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }
}

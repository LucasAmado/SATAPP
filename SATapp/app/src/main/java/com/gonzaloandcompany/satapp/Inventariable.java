package com.gonzaloandcompany.satapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventariable {

    private Integer id;

    private String codigo, tipo, nombre, descripcion, createdAt, ubicacion, updatedAt, imagen;
}

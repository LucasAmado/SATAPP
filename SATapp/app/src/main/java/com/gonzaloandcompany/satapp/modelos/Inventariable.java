package com.gonzaloandcompany.satapp.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventariable {

    private String id, codigo, tipo, nombre, descripcion, ubicacion, createdAt, updatedAt, imagen;

}

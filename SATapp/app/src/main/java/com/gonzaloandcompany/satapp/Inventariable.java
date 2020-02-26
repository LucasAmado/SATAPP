package com.gonzaloandcompany.satapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventariable {

    private String tipo, nombre, descripcion, ubicacion, imagen;
}

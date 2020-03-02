package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inventariable {
    private String id;
    private String codigo;
    private String tipo;
    private String nombre;
    private String descripcion;
    private String ubicación;
    private String createdAt;
    private String updatedAt;
    private String imagen;
}

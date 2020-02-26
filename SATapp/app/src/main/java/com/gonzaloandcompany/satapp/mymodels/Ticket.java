package com.gonzaloandcompany.satapp.mymodels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String id;
    private UsuarioDummy creado_porObject;
    private String fecha_creacion;
    private String estado;
    private String titulo;
    private String descripcion;
    private List<Asignacion> asignaciones;
    private List<String> fotos;
    private String createdAt;
    private String updatedAt;
}

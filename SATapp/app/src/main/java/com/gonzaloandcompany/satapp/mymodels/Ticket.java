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
    //TODO:CAMBIAR USUARIODUMMY POR USUARIO
    private UsuarioDummy creado_por;
    private String fecha_creacion;
    private String estado;
    private String titulo;
    private String descripcion;
    private String inventariable;
    private List<Anotacion> anotaciones;
    private List<Asignacion> asignaciones;
    private List<String> fotos;
    private String createdAt;
    private String updatedAt;

    public Ticket(String titulo, String createdAt) {
        this.titulo = titulo;
        this.createdAt = createdAt;
    }
}

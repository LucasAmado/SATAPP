package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Anotacion {
    private String id;
    private UsuarioDummy id_usuario;
    private String fecha;
    private String cuerpo;
    private Ticket ticket;
    private String createdAt;
    private String updatedAt;

}

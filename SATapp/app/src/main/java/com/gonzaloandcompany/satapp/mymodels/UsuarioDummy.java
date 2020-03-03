package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDummy {
    //TODO: BORRAR CUANDO GONZALO SUBA SU MODELO USUARIO
    //TODO: CAMBIAR EN CLASE TICKET
    private String id;
    private String name;
    private String email;
    private String role;
    private String picture;
    private String createdAt;
    private String updatedAt;
    private boolean validated;
}

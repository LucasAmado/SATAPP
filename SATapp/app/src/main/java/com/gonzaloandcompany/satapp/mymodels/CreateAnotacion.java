package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnotacion {

    private String id_ticket, cuerpo;

    public CreateAnotacion(String cuerpo) {
        this.cuerpo = cuerpo;
    }
}

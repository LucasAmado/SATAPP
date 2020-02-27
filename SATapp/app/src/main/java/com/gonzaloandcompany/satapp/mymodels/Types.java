package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Types {
    PC ("PC"),
    IMPRESORA ("IMPRESORA"),
    PANTALLA ("PANTALLA"),
    PROYECTOR("PROYECTOR"),
    TECLADO("TECLADO"),
    RATON("RATÓN"),
    ORDENADOR("ORDENADOR"),
    FOTOCOPIADORA("FOTOCOPIADORA");

    private String description;

    public String getDescription() {
        return description;
    }
}

package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Estado {
    PENDIENTE_ASIGNACION("Pendiente de asignación"),
    ASIGNADA("Incidencia asignada"),
    EN_PROCESO("Reparando dispositivo"),
    SOLUCIONADA("Incidencia solucionada");

    private String description;

    public String getDescription() {
        return description;
    }
}

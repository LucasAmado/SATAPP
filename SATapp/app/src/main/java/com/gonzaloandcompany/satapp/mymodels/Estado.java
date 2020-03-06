package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Estado {
    PENDIENTE_ASIGNACION("Pendiente de asignación","PENDIENTE_ASIGNACION"),
    ASIGNADA("Incidencia asignada","ASIGNADA"),
    EN_PROCESO("Reparando dispositivo","EN_PROCESO"),
    SOLUCIONADA("Incidencia solucionada","SOLUCIONADA");

    private String description;
    private String name;

    public String getDescription() {
        return description;
    }
    public String getName(){return  name;}

    public static Estado getEstadoByDescription(String description){
        Estado state=null;
        for(Estado e: Estado.values()){
            if(e.getDescription().equals(description)) {
                state = e;
            }
        }
        return  state;
    }
}

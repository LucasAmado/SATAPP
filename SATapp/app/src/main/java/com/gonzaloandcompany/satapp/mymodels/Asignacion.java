package com.gonzaloandcompany.satapp.mymodels;

import org.joda.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {
    private String tecnico_id;
    private LocalDate fecha_asignacion;
}

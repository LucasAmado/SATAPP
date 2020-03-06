package com.gonzaloandcompany.satapp.mymodels;

import org.joda.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Anotacion implements Comparable<Anotacion>  {
    private String id;
    private UsuarioDummy id_usuario;
    private String fecha;
    private String cuerpo;
    private Ticket ticket;
    private String createdAt;
    private String updatedAt;

    @Override
    public int compareTo(Anotacion o) {
        LocalDate date1= LocalDate.parse(getFecha().substring(0, 10));
        LocalDate date2= LocalDate.parse(o.getFecha().substring(0, 10));
        if (getFecha() == null || o.getFecha() == null)
            return 0;
        return getFecha().compareTo(o.getFecha());
    }
}

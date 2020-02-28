package com.gonzaloandcompany.satapp.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.MultipartBody;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketCreateRequest {
    private String titulo;
    private String descripcion;
    private String inventariable;
    private String tecnico;
    private List<MultipartBody.Part> fotos;

}

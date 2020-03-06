package com.gonzaloandcompany.satapp.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketUpdateStateRequest {
    public String estado;
}

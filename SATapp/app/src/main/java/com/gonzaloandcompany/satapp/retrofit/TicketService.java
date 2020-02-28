package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TicketService {
    @GET("/ticket")
    Call<List<Ticket>> getTickets(@Query("page") int page);

    @GET("/ticket/{id}")
    Call<Ticket> getTicket(@Path("id") String id);


}

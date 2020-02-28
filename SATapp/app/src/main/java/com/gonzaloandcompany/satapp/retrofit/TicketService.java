package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TicketService {
    @GET("/ticket")
    Call<List<Ticket>> getTickets(@Query("page") int page);

    @GET("/ticket/{id}")
    Call<Ticket> getTicket(@Path("id") String id);

    @POST("/ticket")
    Call<Ticket> createTicket(@Part("titulo")String titulo,
                              @Part("descripcion") String descripcion,
                              @Part("inventariable") String idInventariable,
                              @Part("tecnico") String idTecnico,
                              @Part("fotos")List<MultipartBody.Part> files);
}

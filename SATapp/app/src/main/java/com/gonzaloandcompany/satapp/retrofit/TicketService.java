package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketAssignRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateStateRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TicketService {
    @GET("/ticket")
    Call<List<Ticket>> getTickets(@Query("page")int page,@Query("limit") int limit);

    @GET("/ticket/{id}")
    Call<Ticket> getTicket(@Path("id") String id);

    @Multipart
    @POST("/ticket")
    Call<Ticket> createTicket(@Part("titulo") RequestBody titulo,
                              @Part("descripcion") RequestBody descripcion,
                              @Part("inventariable") RequestBody idInventariable,
                              @Part("tecnico") RequestBody idTecnico,
                              @Part List<MultipartBody.Part> files);

    @DELETE("/ticket/{id}")
    Call<Void> deleteTicket(@Path("id") String id);

    @GET("/ticket/asignados/me")
    Call<List<Ticket>> getTicketsAssigned(@Query("page")int page,@Query("limit") int limit);

    @GET("/ticket/user/me")
    Call<List<Ticket>> getTicketsCreated(@Query("page")int page,@Query("limit") int limit);

    @PUT("/ticket/{id}/asignar")
    Call<Ticket> assignTech(@Path("id") String id, @Body TicketAssignRequest request);

    @PUT("/ticket/{id}/estado")
    Call<Ticket> updateState(@Path("id") String id, @Body TicketUpdateStateRequest request);

    @PUT("/ticket/{id}")
    Call<Ticket> updateTicket(@Path("id") String id, @Body TicketUpdateRequest request);

}

package com.gonzaloandcompany.satapp.ui.tickets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.PagedList;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.ui.ticketCreate.TicketCreateActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TicketsFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private TicketListener listener;
    private TicketRecyclerViewAdapter adapter;
    private PagedList<Ticket> tickets;
    private RecyclerView recyclerView;
    private TicketsViewModel ticketsViewModel;
    private final int pageSize = 10;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 0;
    private ProgressBar progressBar;
    private FloatingActionButton add;
    private UserViewModel userViewModel;
    private UsuarioDummy currentUser;

    public TicketsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketsViewModel =new ViewModelProvider(this).get(TicketsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_list, container, false);

        Context context = view.getContext();

        tickets = new PagedList<>();
        recyclerView = view.findViewById(R.id.ticketList);
        progressBar = view.findViewById(R.id.ticketListProgressBar);
        add = view.findViewById(R.id.ticketListAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreate = new Intent(context, TicketCreateActivity.class);
                startActivity(goToCreate);
            }
        });



        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TicketRecyclerViewAdapter(tickets.getResults(), listener);
        recyclerView.setAdapter(adapter);


        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int visibleItems = layoutManager.getChildCount();
                int totalItems = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                boolean isLastItem = firstVisibleItemPosition + visibleItems >= totalItems;
                boolean isValidFirstItem = firstVisibleItemPosition >= 0;
                boolean totalIsMoreThanVisible = totalItems >= pageSize;
                boolean shouldLoadMore = isValidFirstItem && isLastItem && totalIsMoreThanVisible;

                if (shouldLoadMore) loadTickets(false);

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TicketListener) {
            listener = (TicketListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TicketListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void loadTickets(final boolean isFirstPage) {
        isLoading = true;
        currentPage++;

        Log.d("ROL USER",currentUser.getRole());
        if(currentUser.getRole().equals("tecnico"))
            ticketsViewModel.getTicketsAssigned(currentPage,pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
                @Override
                public void onChanged(List<Ticket> data) {
                    if (data != null) {
                        tickets.setResults(data);
                        Log.d("TICKETS DE USER",tickets.toString());
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"No hay más tickets que cargar",Toast.LENGTH_LONG).show();
                    }

                    if (!isFirstPage) {
                        adapter.addAll(tickets.getResults());
                    } else {
                        adapter.setData(tickets.getResults());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    isLoading = false;
                    isLastPage = currentPage == tickets.getResults().size();
                }
            });
        else if (currentUser.getRole().equals("user"))
            ticketsViewModel.getTicketsCreated(currentPage,pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
                @Override
                public void onChanged(List<Ticket> data) {
                    if (data != null) {
                        tickets.setResults(data);
                        Log.d("ENTRA EN TICKETS DE USUARIO","TRUE");
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"No hay más tickets que cargar",Toast.LENGTH_LONG).show();
                    }

                    if(tickets.getResults().isEmpty())
                        Toast.makeText(getContext(),"No has creado ningún ticket",Toast.LENGTH_LONG).show();

                    if (!isFirstPage) {
                        adapter.addAll(tickets.getResults());
                    } else {
                        adapter.setData(tickets.getResults());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    isLoading = false;
                    isLastPage = currentPage == tickets.getResults().size();
                }
            });
        else
            ticketsViewModel.getTickets(currentPage,pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
            @Override
            public void onChanged(List<Ticket> data) {
                if (data != null) {
                    tickets.setResults(data);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"No hay más tickets que cargar",Toast.LENGTH_LONG).show();
                }

                if (!isFirstPage) {
                    adapter.addAll(tickets.getResults());
                } else {
                    adapter.setData(tickets.getResults());
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                isLoading = false;
                isLastPage = currentPage == tickets.getResults().size();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage=0;
        getCurrentUser();
    }

    public void getCurrentUser(){
        userViewModel.getCurrentUser().observe(getActivity(), new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                currentUser=usuario;
                loadTickets(true);


            }
        });
    }
}
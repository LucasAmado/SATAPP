package com.gonzaloandcompany.satapp.ui.tickets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.Estado;
import com.gonzaloandcompany.satapp.mymodels.PagedList;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.TicketAssignRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateStateRequest;
import com.gonzaloandcompany.satapp.ui.home.FilterDialogFragment;
import com.gonzaloandcompany.satapp.ui.ticketCreate.TicketCreateActivity;
import com.gonzaloandcompany.satapp.ui.ticketsdetail.TicketDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

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
    private Estado selected;
    private MenuItem assigns;
    private boolean isFirstClick;

    public TicketsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketsViewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        isFirstClick=true;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_icon) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Filtrar por estado");

            String[] statesDescription = new String[Estado.values().length + 1];

            for (int i = 0; i < Estado.values().length; i++) {
                statesDescription[i] = Estado.values()[i].getDescription();
            }
            statesDescription[Estado.values().length] = "Restablecer filtro";
            Log.d("ARRAY ESTADOS", statesDescription.toString());
            dialog.setItems(statesDescription, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String stateSelected = statesDescription[which];
                    Log.d("DESCRIPCION SELECCIONADA", stateSelected.toString());
                    selected = null;
                    for (Estado e : Estado.values()) {
                        if (e.getDescription().equals(stateSelected)) {
                            selected = e;
                            Log.d("ESTADO SELECCIONADO", selected.toString());
                        }
                    }
                    if (selected != null)
                        adapter.setData(tickets.getResults().stream().filter(x -> x.getEstado().equals(selected.getName())).collect(Collectors.toList()));
                    else
                        adapter.setData(tickets.getResults());
                }
            });

            AlertDialog alert = dialog.create();
            alert.show();


        } else {
            currentPage=0;
            if(isFirstClick) {
                isFirstClick=false;
                loadAssigns(true);
                assigns.setIcon(R.drawable.ic_undo_white_24dp);
            }else{
                isFirstClick=true;
                loadTickets(true);
                assigns.setIcon(R.drawable.ic_assignment);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tickets_filter_menu, menu);
        assigns = menu.getItem(0);
        Log.d("BOTON FILTRAR ASIGNACIONES",menu.getItem(0).toString());
        assigns.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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

    public void loadAssigns(final boolean isFirstPage) {
        isLoading = true;
        currentPage++;

            ticketsViewModel.getTicketsAssigned(currentPage, pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
                @Override
                public void onChanged(List<Ticket> data) {
                    if (data != null) {
                        tickets.setResults(data);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No hay más tickets que cargar", Toast.LENGTH_LONG).show();
                    }

                    if (tickets.getResults().isEmpty())
                        Toast.makeText(getContext(), "No tienes ningún ticket asignado", Toast.LENGTH_LONG).show();

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

    public void loadTickets(final boolean isFirstPage) {
        isLoading = true;
        currentPage++;

        Log.d("ROL USER", currentUser.getRole());

        if(currentUser.getRole().equals("tecnico")){
            Log.d("ERES TÉCNICO","DEBERIAS VER UN FILTOR");
            assigns.setVisible(true);
        }

       if (currentUser.getRole().equals("user")||currentUser.getRole().equals("tecnico")) {
            ticketsViewModel.getTicketsCreated(currentPage, pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
                @Override
                public void onChanged(List<Ticket> data) {
                    if (data != null) {
                        tickets.setResults(data);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No hay más tickets que cargar", Toast.LENGTH_LONG).show();
                    }

                    if (tickets.getResults().isEmpty())
                        Toast.makeText(getContext(), "No has creado ningún ticket", Toast.LENGTH_LONG).show();

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
        } else if (currentUser.getRole().equals("admin")) {
            ticketsViewModel.getTickets(currentPage, pageSize).observe(getActivity(), new Observer<List<Ticket>>() {
                @Override
                public void onChanged(List<Ticket> data) {
                    if (data != null) {
                        tickets.setResults(data);
                        Log.d("ENTRA EN TICKETS DE admin", "TRUE");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No hay más tickets que cargar", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = 0;
        getCurrentUser();
    }

    public void getCurrentUser() {
        userViewModel.getCurrentUser().observe(getActivity(), new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                currentUser = usuario;
                loadTickets(true);


            }
        });
    }
}
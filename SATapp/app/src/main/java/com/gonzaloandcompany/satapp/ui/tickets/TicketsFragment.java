package com.gonzaloandcompany.satapp.ui.tickets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.PagedList;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
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
    private final int pageSize = 20;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 0;
    private ProgressBar progressBar;
    private FloatingActionButton add;

    public TicketsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketsViewModel =
                ViewModelProviders.of(this).get(TicketsViewModel.class);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_list, container, false);

        Context context = view.getContext();

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

        tickets = new PagedList<>();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TicketRecyclerViewAdapter(tickets.getResults(), listener);
        recyclerView.setAdapter(adapter);

        //TODO: LISTAR SEGÚN ROL
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
        loadTickets(true);


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
        ticketsViewModel.getTickets(currentPage).observe(getActivity(), new Observer<List<Ticket>>() {
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
}
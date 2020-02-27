package com.gonzaloandcompany.satapp.ui.tickets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.PagedList;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.ArrayList;
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
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            tickets = new PagedList<>();
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
                    boolean isNotLastPage = !isLastPage && !isLoading;
                    boolean isLastItem = firstVisibleItemPosition + visibleItems >= totalItems;
                    boolean isValidFirstItem = firstVisibleItemPosition >= 0;
                    boolean totalIsMoreThanVisible = totalItems >= pageSize;
                    boolean shouldLoadMore = isValidFirstItem && isLastItem && totalIsMoreThanVisible;

                    if (shouldLoadMore) loadTickets(false);

                }
            });
            loadTickets(true);

        }
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
        Log.d("LOADTICKETS","TRUE");
        isLoading = true;
        currentPage++;
        ticketsViewModel.getTickets(currentPage).observe(getActivity(), new Observer<List<Ticket>>() {
            @Override
            public void onChanged(List<Ticket> data) {
                if (data != null){
                    tickets.setResults(data);
                    Log.d("LOADTICKETS","1");

                }

                else if (!isFirstPage){
                    adapter.addAll(tickets.getResults());
                    Log.d("LOADTICKETS","2");

                }else{
                    Log.d("LOADTICKETS","3");
                    adapter.setData(tickets.getResults());


                }




                isLoading = false;
                isLastPage = currentPage == tickets.getResults().size();
            }
        });
    }
}
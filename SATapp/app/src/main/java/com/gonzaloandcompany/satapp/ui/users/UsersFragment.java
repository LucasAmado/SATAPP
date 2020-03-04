package com.gonzaloandcompany.satapp.ui.users;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.PagedList;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private UserListener listener;
    private UsersRecyclerViewAdapter adapter;
    private PagedList<UsuarioDummy> users;
    private RecyclerView recyclerView;
    private UserViewModel usersViewModel;
    private final int pageSize = 20;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 0;
    private ProgressBar progressBar;
    private Parcelable recylerState;

    public UsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        Context context = view.getContext();
        users = new PagedList<>();
        users.setResults(new ArrayList<>());

        recyclerView = view.findViewById(R.id.userList);
        progressBar = view.findViewById(R.id.userListProgressBar);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersRecyclerViewAdapter(users.getResults(), getContext(), listener);
        recyclerView.setAdapter(adapter);

        recylerState = recyclerView.getLayoutManager().onSaveInstanceState();
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

                if (shouldLoadMore) loadUsers(false);

            }
        });
        loadUsers(true);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserListener) {
            listener = (UserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UserListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void loadUsers(final boolean isFirstPage) {
        isLoading = true;
        currentPage++;
        usersViewModel.getUsersPaginable(currentPage, pageSize).observe(getActivity(), new Observer<List<UsuarioDummy>>() {
            @Override
            public void onChanged(List<UsuarioDummy> data) {
                if (data != null) {
                    users.getResults().addAll(data);
                    users.getResults().sort((UsuarioDummy o1, UsuarioDummy o2) -> {
                        if (o1.getName() != null && o2.getName() != null)
                            return o1.getName().compareTo(o2.getName());
                        else
                            return o1.getEmail().compareTo(o2.getEmail());
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No hay más usuarios que cargar", Toast.LENGTH_LONG).show();
                }

                adapter.setData(users.getResults().stream().distinct().collect(Collectors.toList()));
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                isLoading = false;
                isLastPage = currentPage == users.getResults().size();
            }
        });
    }
}
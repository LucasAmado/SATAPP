package com.gonzaloandcompany.satapp.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;

import java.util.ArrayList;
import java.util.List;

public class InventariableFragment extends Fragment implements DialogPassData {

    private List<Inventariable> inventariableList;
    private Context context;
    private RecyclerView recyclerView;
    MyInventariableRecyclerViewAdapter adapter;
    private MyInventariableRecyclerViewAdapter myInventariableRecyclerViewAdapter;
    JLuisViewModel jLuisViewModel;
    private List<Inventariable> byInventariable;
    private DialogPassData dialogPassData;
    private List<String> ubicaciones = new ArrayList<>();

    public InventariableFragment() {
    }

    @Override
    public void filterByUbicacion(String ubicacion) {
        byInventariable = new ArrayList<>();
        Log.d("UBICAION",inventariableList.size()+"");
        for (Inventariable i : inventariableList) {
            if(i.getUbicación().equals(ubicacion)){
                byInventariable.add(i);
            }
        }
        myInventariableRecyclerViewAdapter = new MyInventariableRecyclerViewAdapter(context, byInventariable, jLuisViewModel);
        recyclerView.setAdapter(myInventariableRecyclerViewAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filtro, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ubicacion:
                DialogFragment dialog = new FilterDialogFragment(dialogPassData, ubicaciones);
                dialog.setTargetFragment(this, 0);
                dialog.show(getFragmentManager(), "MonedasFilterDialogFragment");

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jLuisViewModel = new ViewModelProvider(getActivity()).get(JLuisViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventariable_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

            adapter = new MyInventariableRecyclerViewAdapter(
                    getActivity(),
                    inventariableList,
                    jLuisViewModel
            );
            recyclerView.setAdapter(adapter);

            loadInventariables();
            loadUbicaciones();
        }
        return view;

    }

    private void loadInventariables() {
        jLuisViewModel.getAllInventariables().observe(getActivity(), inventariable -> {
            inventariableList = inventariable;
            adapter.setData(inventariableList);
        });
    }

    private void loadUbicaciones() {
        jLuisViewModel.getAllUbicaciones().observe(getActivity(), strings -> ubicaciones = strings);
        Log.d("UBICACION",ubicaciones.size()+"");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogPassData = null;
    }


}
package com.gonzaloandcompany.satapp.ui.home;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.LucasViewModel;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyInventariableRecyclerViewAdapter extends RecyclerView.Adapter<MyInventariableRecyclerViewAdapter.ViewHolder> implements Filterable {

    private Context ctx;
    private List<Inventariable> mValues;
    private List<Inventariable> exampleListFull;
    private JLuisViewModel jLuisViewModel;

    public MyInventariableRecyclerViewAdapter(Context ctx, List<Inventariable> mValues, JLuisViewModel jLuisViewModel) {
        this.ctx = ctx;
        this.mValues = mValues;
        this.jLuisViewModel = jLuisViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventariable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null) {
            holder.mItem = mValues.get(position);


            GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + holder.mItem.getImagen(),
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                        .build());

            Glide.with(ctx)
                    .load(glideUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.ivImage);


            holder.tvNombre.setText(String.valueOf(holder.mItem.getNombre()));
            holder.tvTipo.setText(String.valueOf(holder.mItem.getTipo()));
            holder.tvDescripcion.setText(String.valueOf(holder.mItem.getDescripcion()));
            holder.mView.setOnClickListener(v -> {
                if (null != jLuisViewModel) {
                    jLuisViewModel.setIdInventariable(holder.mItem.getId());
                }
            });
        }
    }

    public void setData(List<Inventariable> inventariableList){
        this.mValues = inventariableList;
        exampleListFull = new ArrayList<>(inventariableList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null) {
            return mValues.size();
        }else return 0;
    }

    @Override
    public Filter getFilter() {
        return listaFiltrada;
    }

    private Filter listaFiltrada = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Inventariable> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length()== 0){
                filteredList.addAll(exampleListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Inventariable i : exampleListFull){
                    if(i.getNombre().toLowerCase().contains(filterPattern)){
                        filteredList.add(i);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(mValues!=null){
            mValues.clear();
            mValues.addAll((Collection<? extends Inventariable>) results.values);
            notifyDataSetChanged();
        }}
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivImage;
        public final TextView tvNombre;
        public final TextView tvTipo;
        public final TextView tvDescripcion;
        public Inventariable mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivImage = view.findViewById(R.id.imageViewInventariable);
            tvNombre = view.findViewById(R.id.textViewNombre);
            tvTipo = view.findViewById(R.id.textViewTipo);
            tvDescripcion = view.findViewById(R.id.textViewDescripcion);
        }
    }
}

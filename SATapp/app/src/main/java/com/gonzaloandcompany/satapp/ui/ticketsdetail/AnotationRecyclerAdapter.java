package com.gonzaloandcompany.satapp.ui.ticketsdetail;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.AnotacionViewModel;
import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.requests.CreateAnotacion;
import com.gonzaloandcompany.satapp.ui.anotaciones.AnotacionDialogFragment;


import org.joda.time.LocalDate;

import java.util.List;

public class AnotationRecyclerAdapter extends RecyclerView.Adapter<AnotationRecyclerAdapter.ViewHolder> {
    private List<Anotacion> anotations;

    private Context context;

    public AnotationRecyclerAdapter(List<Anotacion> anotations, Context context) {
        this.anotations = anotations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_anotation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String aux;
        if (anotations != null) {
            holder.anotation = anotations.get(position);

            if (holder.anotation.getId_usuario().getName() != null)
                holder.user.setText(holder.anotation.getId_usuario().getName());
            if (holder.anotation.getId_usuario().getEmail() != null)
                holder.user.setText(holder.anotation.getId_usuario().getEmail());

            holder.body.setText(holder.anotation.getCuerpo());
            LocalDate date = LocalDate.parse(holder.anotation.getFecha().substring(0, 10));
            holder.date.setText(date.toString("dd/MM/yyyy"));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialog = new AnotacionDialogFragment(holder.anotation.getTicket().getId(), holder.anotation.getId());
                    FragmentManager manager= ((AppCompatActivity)context).getSupportFragmentManager();
                    dialog.show(manager, "AnotacionDialogFragment");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (anotations != null)
            return anotations.size();
        else
            return 0;
    }

    public void setData(List<Anotacion> list) {
        this.anotations = list;
        notifyDataSetChanged();

    }

    public void addAll(List<Anotacion> list) {
        int lastIndex = anotations.size() - 1;
        anotations.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Anotacion anotation;
        public TextView user;
        public TextView body;
        public TextView date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            user = view.findViewById(R.id.anotation_user);
            body = view.findViewById(R.id.anotation_body);
            date = view.findViewById(R.id.anotation_date);
        }

    }

}

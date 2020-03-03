package com.gonzaloandcompany.satapp.ui.ticketCreate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Ticket;


import java.util.List;

public class TicketCreateAdapter extends RecyclerView.Adapter<TicketCreateAdapter.ViewHolder> {
    private List<Image> images;
    private Context context;

    public TicketCreateAdapter(List<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preview_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String aux;
        if (images != null) {
            holder.image = images.get(position);
            Glide.with(context).load(holder.image.getPath()).centerCrop().into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        if (images != null)
            return images.size();
        else
            return 0;
    }

    public void setData(List<Image> list) {
        this.images = list;
        notifyDataSetChanged();

    }

    public void addAll(List<Image> list) {
        int lastIndex = images.size() - 1;
        images.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Image image;
        public ImageView img;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            img = view.findViewById(R.id.ticketCreateImg);

        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", image=" + image;
        }
    }
}

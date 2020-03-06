package com.gonzaloandcompany.satapp.ui.ticketCreate;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.gonzaloandcompany.satapp.R;
import java.util.List;

public class TicketCreateAdapter extends RecyclerView.Adapter<TicketCreateAdapter.ViewHolder> {
    private List<Uri> images;
    private Context context;

    public TicketCreateAdapter(List<Uri> images, Context context) {
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
            Glide.with(context).load(holder.image).centerCrop().into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        if (images != null)
            return images.size();
        else
            return 0;
    }

    public void setData(List<Uri> list) {
        this.images = list;
        notifyDataSetChanged();

    }

    public void addAll(List<Uri> list) {
        int lastIndex = images.size() - 1;
        images.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Uri image;
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

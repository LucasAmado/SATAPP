package com.gonzaloandcompany.satapp.ui.users;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.ui.tickets.TicketListener;


import java.util.List;


public class UsersRecyclerViewAdapter  extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    //TODO: CAMBIAR LA CLASE USUARIODUMMY POR LA DE USUARIO DE GONZALO
    private List<UsuarioDummy> users;
    private Context context;
    private final UserListener listener;

    public UsersRecyclerViewAdapter(List<UsuarioDummy> users, Context context, UserListener listener) {
        this.users = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String aux;
        if (users != null) {
            holder.user = users.get(position);

            if(holder.user.getName()!=null)
                holder.name.setText(holder.user.getName());
            else
                holder.name.setText(holder.user.getEmail());

            String rol = holder.user.getRole();

            if(rol.equals("tecnico"))
                holder.rol.setText("Técnico");
            else if (rol.equals("user"))
                holder.rol.setText("Usuario");
            else
                holder.rol.setText("Administrador");

            if(holder.user.isValidated()){
                holder.check.setImageResource(R.drawable.ic_tick);
            }else{
                holder.check.setImageResource(R.drawable.ic_close);
            }
            if(holder.user.getPicture()!=null){
                GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + holder.user.getPicture(),
                        new LazyHeaders.Builder()
                                .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                                .build());

                Glide.with(context).load(glideUrl).circleCrop().into(holder.img);
            }else{
                Glide.with(context).load(R.drawable.iconfinder_unknown_403017).circleCrop().into(holder.img);
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnUserClick(holder.user.getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        else
            return 0;
    }

    //TODO: CAMBIAR CLASE USUARIODUMMY POR LA DE GONZALO

    public void setData(List<UsuarioDummy> list) {
        this.users = list;
        notifyDataSetChanged();

    }
    //TODO: CAMBIAR CLASE USUARIODUMMY POR LA DE GONZALO

    public void addAll(List<UsuarioDummy> list) {
        int lastIndex = users.size() - 1;
        users.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //TODO: Cambiar clase usuariodummy por la de gonzalo
        public UsuarioDummy user;
        public ImageView img;
        public TextView name;
        public TextView rol;
        public ImageView check;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            img = view.findViewById(R.id.userListImg);
            name = view.findViewById(R.id.userListName);
            rol = view.findViewById(R.id.userListRol);
            check = view.findViewById(R.id.userListCheckValidated);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", user=" + user +
                    ", img=" + img +
                    ", name=" + name +
                    ", rol=" + rol +
                    ", check=" + check +
                    '}';
        }
    }
}

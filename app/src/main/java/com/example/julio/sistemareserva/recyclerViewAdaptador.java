package com.example.julio.sistemareserva;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerViewAdaptador extends RecyclerView.Adapter<recyclerViewAdaptador.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombre, descripcion;
        private ImageButton cartelera;



        public ViewHolder(View itemView) {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre);
            descripcion=(TextView)itemView.findViewById(R.id.desc);
            cartelera=(ImageButton) itemView.findViewById(R.id.idCartelera);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    private List<modelCartelera> carteleraLista;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    Context context;

    public recyclerViewAdaptador(List<modelCartelera> carteleraLista, RecyclerViewOnItemClickListener listener, Context context) {
        this.carteleraLista=carteleraLista;
        this.recyclerViewOnItemClickListener=listener;
        this.context=context;
    }

    public List<modelCartelera> getCarteleraLista() {
        return carteleraLista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_cartelera, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(carteleraLista.get(position).getNombre());
        holder.descripcion.setText(carteleraLista.get(position).getDescripcion());
        //holder.cartelera.setImageResource(carteleraLista.get(position).getImagenCartelera());
        /*if (carteleraLista.get(position).getBitImagen() != null) {
            holder.cartelera.setImageBitmap(carteleraLista.get(position).getBitImagen);
        }
        else {
            holder.cartelera.setImageResource(R.drawable.pantalla);
        }*/
        if(!carteleraLista.get(position).getUrlImagen().isEmpty()) {
            Picasso.with(context).load(carteleraLista.get(position).getUrlImagen()).error(R.drawable.pantalla).fit().centerInside().into(holder.cartelera);
        } else {
            holder.cartelera.setImageResource(R.drawable.pantalla);
        }
    }
    @Override
    public int getItemCount() {
        return carteleraLista.size();
    }
}

package com.java.movilasofar.controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.movilasofar.R;
import com.java.movilasofar.modelo.Proveedor;

import java.util.List;

class AdapterServidor extends RecyclerView.Adapter<AdapterServidor.servidorViewHolder> {

    Context context;
    List<Proveedor> listaServidor;

    public AdapterServidor(Context context, List<Proveedor> listaServidor){
        this.context = context;
        this.listaServidor = listaServidor;

    }

    @NonNull
    @Override
    public servidorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner, null, false);
        return new AdapterServidor.servidorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull servidorViewHolder holder, int position){
        holder.tvProveedor.setText(listaServidor.get(position).getNombre());
    }

    @Override
    public int getItemCount(){ return listaServidor.size(); }

    public class servidorViewHolder extends RecyclerView.ViewHolder{
        TextView tvProveedor;

        public servidorViewHolder(@NonNull View itemView){
            super(itemView);
            tvProveedor = itemView.findViewById(R.id.tvProveedor);
        }
    }

}

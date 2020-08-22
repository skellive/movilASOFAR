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

class AdapterLocal extends RecyclerView.Adapter<AdapterLocal.servidorViewHolder> {
   Context context;
   List<Proveedor> listalocal;

   public AdapterLocal(Context context, List<Proveedor> listalocal){
       this.context = context;
       this.listalocal = listalocal;
   }

    @NonNull
    @Override
    public servidorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner, null, false);
        return new AdapterLocal.servidorViewHolder(view);
   }

   @Override
    public void onBindViewHolder(@NonNull servidorViewHolder holder, int position){
       holder.tvProveedor.setText(listalocal.get(position).getNombre());
   }

   @Override
    public int getItemCount(){ return listalocal.size(); }

    public class servidorViewHolder extends RecyclerView.ViewHolder{
       TextView tvProveedor, tvSync;

       public servidorViewHolder(@NonNull View itemView){
           super(itemView);
           tvProveedor = itemView.findViewById(R.id.tvProveedor);
           tvSync = itemView.findViewById(R.id.tvSync);

       }
    }
}

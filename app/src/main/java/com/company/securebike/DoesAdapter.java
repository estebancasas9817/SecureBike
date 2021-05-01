package com.company.securebike;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.securebike.auxiliares.MyDoes;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder>{

    Context context;
    ArrayList<MyDoes> myDoes;

    public DoesAdapter(Context c, ArrayList<MyDoes> p) {
        context = c;
        myDoes = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.titledoes.setText(myDoes.get(i).getColor());
        myViewHolder.nombre.setText(myDoes.get(i).getNombre());
        myViewHolder.datedoes.setText(myDoes.get(i).getMatricula());

        final String getTitleDoes = myDoes.get(i).getColor();
        final String getDescDoes = myDoes.get(i).getMarca();
        final String getDateDoes = myDoes.get(i).getMatricula();
        final String getKeyDoes = myDoes.get(i).getKeydoes();
        final String getUsuario = myDoes.get(i).getUsuario();
        final String getNombre = myDoes.get(i).getNombre();
        final String getRasgos = myDoes.get(i).getRasgos();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context,EditTaskDesk.class);
                aa.putExtra("titledoes", getTitleDoes);
                aa.putExtra("descdoes", getDescDoes);
                aa.putExtra("datedoes", getDateDoes);
                aa.putExtra("keydoes", getKeyDoes);
                aa.putExtra("usuario",getUsuario);
                aa.putExtra("nombre",getNombre);
                aa.putExtra("rasgos",getRasgos);
                context.startActivity(aa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titledoes, nombre, datedoes, keydoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titledoes = (TextView) itemView.findViewById(R.id.titledoes);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            datedoes = (TextView) itemView.findViewById(R.id.datedoes);
        }
    }

}
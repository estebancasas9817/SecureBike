package com.company.securebike;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;


import com.company.securebike.auxiliares.MyDoes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AgregarBicicleta extends AppCompatActivity {

    TextView titlepage, subtitlepage, endpage;
    Button btnAddNew;

    DatabaseReference reference;
    RecyclerView ourdoes;
    DatabaseReference baseDatos;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_bicicleta);

        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);
        endpage = findViewById(R.id.endpage);
        Intent in = getIntent();
        usuario = in.getStringExtra("usuario");

        btnAddNew = findViewById(R.id.btnAddNew);





        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(AgregarBicicleta.this,NewTaskAct.class);
                a.putExtra("usuario",usuario);
                startActivity(a);
                startActivity(a);
            }
        });


//         working with data
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyDoes>();

//         get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrive data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                doesAdapter = new DoesAdapter(AgregarBicicleta.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

//        @Override
//        public void onBackPressed() {
//            super.onBackPressed();
//            baseDatos.child("Usuarios").child(usuario).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Intent home = new Intent(MyDoes.this,Init.class);
//                    nombre = snapshot.child("nombre").getValue().toString();
//                    clave = snapshot.child("clave").getValue().toString();
//                    correo = snapshot.child("email").getValue().toString();
//                    home.putExtra("usuario", usuario);
//                    home.putExtra("nombre", nombre);
//                    home.putExtra("clave", clave);
//                    home.putExtra("email", correo);
//                    startActivity(home);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

    }
}
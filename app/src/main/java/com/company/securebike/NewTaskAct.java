package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.securebike.auxiliares.MyDoes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NewTaskAct extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText titledoes, descdoes, datedoes,rasgosDoes,nombreDoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    Integer doesNum = new Random().nextInt();
    String keydoes = Integer.toString(doesNum);
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);
        rasgosDoes = findViewById(R.id.rasgosDoes);
        nombreDoes = findViewById(R.id.nombreDoes);

        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");



        btnSaveTask.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // insert data to database
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario);
                String titulo = titledoes.getText().toString();
                String descrip = descdoes.getText().toString();
                String fechas = datedoes.getText().toString();
                String key = keydoes;
                String username = usuario;
                String nombre = nombreDoes.getText().toString();
                String rasgos = rasgosDoes.getText().toString();
                MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos);

                reference.child("Does" + doesNum).setValue(myDoesAux);

                Intent ir = new Intent(NewTaskAct.this,AgregarBicicleta.class);
                ir.putExtra("usuario",usuario);
                Toast.makeText(NewTaskAct.this,"Creado con exito....",Toast.LENGTH_SHORT).show();
                startActivity(ir);
//
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devolver = new Intent(NewTaskAct.this,AgregarBicicleta.class);
                devolver.putExtra("usuario",usuario);
                startActivity(devolver);
            }
        });


    }
}
package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTaskDesk extends AppCompatActivity {

    EditText titleDoes, descDoes, dateDoes,editarNombre,editarRasgos;
    Button btnSaveUpdate, btnDelete;
    DatabaseReference reference;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);
        titleDoes = findViewById(R.id.titleDoes);
        descDoes = findViewById(R.id.descDoes);
        editarNombre = findViewById(R.id.editarNombre);
        dateDoes = findViewById(R.id.dateDoes);
        editarRasgos = findViewById(R.id.editarRasgos);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");


        titleDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));
        editarNombre.setText(getIntent().getStringExtra("nombre"));
        editarRasgos.setText(getIntent().getStringExtra("rasgos"));

        final String keykeyDoes = getIntent().getStringExtra("keydoes");




        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario);

                reference.child("Does" + keykeyDoes).child("color").setValue(titleDoes.getText().toString());
                reference.child("Does" + keykeyDoes).child("marca").setValue(descDoes.getText().toString());
                reference.child("Does" + keykeyDoes).child("matricula").setValue(dateDoes.getText().toString());
                reference.child("Does" + keykeyDoes).child("keydoes").setValue(keykeyDoes);
                reference.child("Does" + keykeyDoes).child("nombre").setValue(editarNombre.getText().toString());
                reference.child("Does" + keykeyDoes).child("rasgos").setValue(editarRasgos.getText().toString());
                Intent a = new Intent(EditTaskDesk.this,AgregarBicicleta.class);
                a.putExtra("usuario",usuario);


                Toast.makeText(EditTaskDesk.this,"Cambios guardados...",Toast.LENGTH_LONG).show();
                startActivity(a);



//
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario).child("Does" + keykeyDoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent a = new Intent(EditTaskDesk.this,AgregarBicicleta.class);
                            a.putExtra("usuario",usuario);
                            Toast.makeText(EditTaskDesk.this,"Bicicleta eliminada...",Toast.LENGTH_LONG).show();
                            startActivity(a);
                        }
                    }
                });

            }
        });
    }
}
package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.util.ArrayList;

public class Configuracion extends AppCompatActivity {

    TextView seleccionarText;
    ArrayList<String> arrayList;
    Dialog dialog;

    EditText configuracionUsuario,configuracionNombre,configuracionCel,configuracionDireccion,configuracionEmail,configuracionClave;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    EditText editarDir,agregarDir;


    String usuario;
     String email;
     String clave;
     String nombre;
     String cel;
     String celular;
     String direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        seleccionarText = findViewById(R.id.selecionarTextView);
        editarDir = findViewById(R.id.editarDireccion);
        agregarDir = findViewById(R.id.agregarDireccion);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        Intent intent = getIntent();

        usuario = intent.getStringExtra("usuario");
        email = intent.getStringExtra("email");
        clave = intent.getStringExtra("clave");
        nombre = intent.getStringExtra("nombre");
        cel = intent.getStringExtra("celular");
        celular = cel;
        direccion = intent.getStringExtra("direccion");

        setDatos(nombre,usuario,email,clave,direccion,cel);




        arrayList = new ArrayList<>();

        arrayList.add(direccion);
//        arrayList.add("calle 149 #27-12");
//        arrayList.add("calle 98 #7-24");
//        arrayList.add("calle 45 #7-13");

        seleccionarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Configuracion.this);
                dialog.setContentView(R.layout.dialog_search);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                EditText editDireccion = dialog.findViewById(R.id.editDirecion);
                ListView lista = dialog.findViewById(R.id.lista);


                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Configuracion.this, android.R.layout.simple_list_item_1,arrayList);
                lista.setAdapter(adapter);

                editDireccion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        seleccionarText.setText(adapter.getItem(position));
                        editarDir.setText(adapter.getItem(position));

                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public void setDatos(String nombre,String usuario,String email,String clave,String direccion,String cel){
        configuracionUsuario = findViewById(R.id.configuracionUsuario);
        configuracionNombre = findViewById(R.id.configuracionNombre);
        configuracionCel = findViewById(R.id.configuracionCel);
        configuracionEmail = findViewById(R.id.configuracionEmail);
        configuracionClave = findViewById(R.id.configuracionClave);


        configuracionNombre.setText(nombre);
        configuracionUsuario.setText(usuario);
        configuracionEmail.setText(email);
        configuracionClave.setText(clave);
        configuracionCel.setText(String.valueOf(cel));

    }


    public void actualizar(View view){
        if (cambioClave() || cambioNombre() || cambioUsuario() || cambioCel() || cambioDir() || cambioEmail()){
            Toast.makeText(Configuracion.this,"Cambios guardados.....",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Configuracion.this,"Los datos son los mismos.....",Toast.LENGTH_LONG).show();
        }
    }

    private boolean cambioEmail() {

        if(!email.equals(configuracionEmail.getText().toString())){
            databaseReference.child(usuario).child("email").setValue(configuracionEmail.getText().toString());

            firebaseAuth = FirebaseAuth.getInstance();

            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, clave);

            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        firebaseUser.updateEmail(configuracionEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                    }
                }
            });

            email = configuracionEmail.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean cambioDir() {
        String direcciones = seleccionarText.getText().toString();
        String agregar = agregarDir.getText().toString();
        if(!direcciones.equals(editarDir.getText().toString())){
            String dir;
            for (int i = 0; i < arrayList.size(); i++) {
                dir = arrayList.get(i);
                if(direcciones == dir) {
                    if(direcciones == direccion){
                        String dirFirebase = editarDir.getText().toString();
                        databaseReference.child(usuario).child("direccion").setValue(dirFirebase);
                        direccion = dirFirebase;
                    }
                    arrayList.remove(i);
                    arrayList.add(editarDir.getText().toString());



                }

            }

            return true;
        }
        if(!agregar.isEmpty()){
            arrayList.add(agregarDir.getText().toString());
            String espacios = "";
            agregarDir.setText(espacios);
            return true;
        }
        else{
            return false;
        }

//        if(!direccion.equals(configuracionDireccion.getText().toString())){
//            databaseReference.child(usuario).child("direccion").setValue(configuracionDireccion.getText().toString());
//            direccion = configuracionDireccion.getText().toString();
//            Toast.makeText(Configuracion.this,":"+arrayList.size(),Toast.LENGTH_LONG).show();
//
//
//            return true;
//        }
//        else{
//            return false;
//        }
    }

    private boolean cambioCel() {

        if(!celular.equals(configuracionCel.getText().toString())){
            String celu = configuracionCel.getText().toString();
            databaseReference.child(usuario).child("celular").setValue(celu);
            celular = configuracionCel.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean cambioUsuario() {

        if(!usuario.equals(configuracionUsuario.getText().toString())){
            databaseReference.child(usuario).child("usuario").setValue(configuracionUsuario.getText().toString());
            usuario = configuracionUsuario.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean cambioNombre() {

        if(!nombre.equals(configuracionNombre.getText().toString())){
            databaseReference.child(usuario).child("nombre").setValue(configuracionNombre.getText().toString());
            nombre = configuracionNombre.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean cambioClave() {
        if(!clave.equals(configuracionClave.getText().toString())){
            databaseReference.child(usuario).child("clave").setValue(configuracionClave.getText().toString());
            firebaseAuth = FirebaseAuth.getInstance();

            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, clave);

            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        firebaseUser.updatePassword(configuracionClave.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                    }
                }
            });


            clave = configuracionClave.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
}
package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText Usuario, correo, clave;

    FirebaseAuth firebaseAuth;

    Button BotonIniciarSesion;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        Usuario = findViewById(R.id.Usuario);
        correo = findViewById(R.id.Correo);
        clave = findViewById(R.id.Contras);

        BotonIniciarSesion = findViewById(R.id.btnLogin);
            BotonIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String usuario = Usuario.getText().toString();
                    final String email = correo.getText().toString();
                    final String password = clave.getText().toString();

                    if(usuario.isEmpty()){
                        mostrarError(Usuario,"Debes llenar este campo");
                    }
                    else if(email.isEmpty()){
                        mostrarError(correo,"Debes llenar este campo");

                    }
                    else if(password.isEmpty()){
                        mostrarError(clave,"Debes llenar este campo");
                    }
                    else{

                        Query chequearUsuario =databaseReference.orderByChild("usuario").equalTo(usuario);

                        chequearUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String claves = snapshot.child(usuario).child("clave").getValue(String.class);
                                    String correos = snapshot.child(usuario).child("email").getValue(String.class);
                                    if (correos.equals(email)){

                                        if(claves.equals(password)){
                                            String cel = snapshot.child(usuario).child("celular").getValue(String.class);
                                            String direccion = snapshot.child(usuario).child("direccion").getValue(String.class);
                                            String nombre = snapshot.child(usuario).child("nombre").getValue(String.class);

                                            Intent intent = new Intent(Login.this, MapsActivity.class);
                                            intent.putExtra("usuario",usuario);
                                            intent.putExtra("nombre",nombre);
                                            intent.putExtra("direccion",direccion);
                                            intent.putExtra("email",email);
                                            intent.putExtra("clave",password);
                                            intent.putExtra("celular",cel);
//
                                            Toast.makeText(Login.this, "Bienvenido de vuelta", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);


                                        }
                                        else{

                                            clave.setError("Contraseña incorrecta");
                                        }

                                    } else{
                                        correo.setError("Correo incorrecto");
                                    }
                                }
                                else{
                                    Usuario.setError("El usuario ingresado no existe");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
            });
    }

    private void mostrarError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
    }

    public void seTeOlvidoLaContraseña(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a restablecer contraseña", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Configuracion.class);
        startActivity(intent);
    }

    public void registrarse(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a registrarse", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Registro.class);
        startActivity(intent);
    }

}
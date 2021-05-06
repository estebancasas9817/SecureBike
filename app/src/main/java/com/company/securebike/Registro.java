package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.securebike.auxiliares.UsuarioAux;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    EditText usuario,nombre,mail,celular,clave,repetirClave,direccion;
    TextView btnYaTengoCuenta;
    Button btnRegistrarse;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuario = findViewById(R.id.inputUsername);
        nombre = findViewById(R.id.inputNombre);
        mail = findViewById(R.id.inputEmail);
        celular = findViewById(R.id.inputCelular);
        clave = findViewById(R.id.inputClave);
        repetirClave = findViewById(R.id.inputConfirmarClave);
        direccion = findViewById(R.id.inputDireccion);
        btnYaTengoCuenta = findViewById(R.id.btnYaTengoCuenta);
        btnRegistrarse = findViewById(R.id.btnRegistrar);


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Usuarios");

                final String usuarios = usuario.getText().toString();
                final String nombres = nombre.getText().toString();
                final String email = mail.getText().toString();
                final String cel = celular.getText().toString();
                final String password = clave.getText().toString();
                final String dir = direccion.getText().toString();
                String valClave = repetirClave.getText().toString();
                int cantidad = password.length();
                boolean bandera = false;
                if (usuarios.isEmpty()){
                    mostrarError(usuario,"Debes llenar este campo");
                }
                if (nombres.isEmpty()){
                    mostrarError(nombre,"Debes llenar este campo");
                }
                if (email.isEmpty()){
                    mostrarError(mail,"Debes llenar este campo");
                }
                if (cel.isEmpty()){
                    mostrarError(celular,"Debes llenar este campo");
                }
                if (password.isEmpty()){
                    mostrarError(clave,"Debes llenar este campo");
                }
                if (dir.isEmpty()){
                    mostrarError(direccion,"Debes llenar este campo");
                }

                else{
                    bandera = true;
                }

                if(bandera == true){


                    firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UsuarioAux usuarioAux = new UsuarioAux(usuarios,nombres,email,cel,password,dir);
                                databaseReference.child(usuarios).setValue(usuarioAux);
                                Toast.makeText(Registro.this,"Usuario Registrado.....",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registro.this,Inicio.class);
                                intent.putExtra("usuario",usuarios);
                                intent.putExtra("nombre",nombres);
                                intent.putExtra("direccion",dir);
                                intent.putExtra("email",email);
                                intent.putExtra("clave",password);
                                intent.putExtra("celular",cel);
                                startActivity(intent);

                            }
                            else{
                                task.getResult();
                                Toast.makeText(Registro.this,task.getResult().toString(),Toast.LENGTH_LONG).show();
//                                Log.i(task.getResult().toString());

                            }
                        }
                    });


                }




            }
        });


        btnYaTengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this,Login.class);
                startActivity(i);
            }
        });

    }

    private void mostrarError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
    }

    private boolean validarUsuario(){
        String val = usuario.getText().toString();
        if(val.isEmpty()){
            usuario.setError("Este campo no puede estar vacio");
            return false;
        }
        else if(val.length() >= 15) {
            usuario.setError("Usuario demasiado largo");
            return false;
        }

        else{
            usuario.setError(null);
            return true;
        }
    }

    private boolean validarEmail(){
        String val = mail.getText().toString();
        String email = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(val.isEmpty()){
            mail.setError("Este campo no puede estar vacio");
            return false;
        }

        else if(!val.matches(email)){
            mail.setError("Correo invalido");
            return false;
        }

        else{
            mail.setError(null);
            return true;
        }
    }
    private boolean validarNombre(){
        String val = nombre.getText().toString();

        if(val.isEmpty()){
            nombre.setError("Este campo no puede estar vacio");
            return false;
        }

        else{
            nombre.setError(null);
            return true;
        }
    }



    private boolean validarClave(){
        String val = clave.getText().toString();
        String valClave = repetirClave.getText().toString();
        Toast.makeText(Registro.this,val.length(),Toast.LENGTH_LONG).show();
        if(val.isEmpty()){
            clave.setError("Este campo no puede estar vacio");
            return false;
        }
        else if(val.length() < 6){
            clave.setError("La clave debe tener más de 6 caracteres");
            return false;
        }
        else if(val != valClave){
            clave.setError("La clave no coincide");
            return false;
        }
        else if(valClave.isEmpty()){
            repetirClave.setError("Este campo no puede estar vacio");
            return false;
        }

        else{
            clave.setError(null);
            repetirClave.setError(null);
            return true;
        }
    }

    private boolean validarDireccion(){
        String val = direccion.getText().toString();

        if(val.isEmpty()){
            direccion.setError("Este campo no puede estar vacio");
            return false;
        }
        else{
            direccion.setError(null);
            return true;
        }
    }

    private boolean validarTel(){
        String val = celular.getText().toString();

        if(val.isEmpty()){
            celular.setError("Este campo no puede estar vacio");
            return false;
        }
        else{
            celular.setError(null);
            return true;
        }
    }





}
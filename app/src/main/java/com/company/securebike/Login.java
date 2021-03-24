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

public class Login extends AppCompatActivity {

    EditText Usuario, correo, clave;

    FirebaseAuth firebaseAuth;

    Button BotonIniciarSesion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Usuario = findViewById(R.id.Usuario);
        correo = findViewById(R.id.Correo);
        clave = findViewById(R.id.Contras);

        BotonIniciarSesion = findViewById(R.id.btnLogin);
            BotonIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usuario = Usuario.getText().toString();
                    String email = correo.getText().toString();
                    String password = clave.getText().toString();

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



                        firebaseAuth = FirebaseAuth.getInstance();

                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this, "Bienvenido a Home", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                }
                                else{

                                    Toast.makeText(Login.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                }
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
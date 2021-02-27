package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registro extends AppCompatActivity {

    EditText usuario,nombre,mail,celular,clave,repetirClave,direccion;
    TextView btnYaTengoCuenta;
    Button btnRegistrarse;

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
                Intent i = new Intent(Registro.this,Home.class);
                startActivity(i);
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
}
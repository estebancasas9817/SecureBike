package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void seTeOlvidoLaContraseña(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a restablecer contraseña", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Configuracion.class);
        intent.putExtra("login", "Hola_1");
        startActivity(intent);
    }

    public void iniciarSesion(View v)
    {
        Toast.makeText(v.getContext(), "Bienvenido a Home", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Home.class);
        intent.putExtra("login", "Hola_2");
        startActivity(intent);
    }

    public void registrarse(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a registrarse", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Registro.class);
        intent.putExtra("login", "Hola_3");
        startActivity(intent);
    }
}
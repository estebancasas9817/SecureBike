package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Inicio extends AppCompatActivity {

    ImageView verMapa,configuracion,sos,invitacion,bicicletas,contactos;
    int Permisos_Call = 0;

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
        setContentView(R.layout.activity_inicio);

        verMapa = findViewById(R.id.verMapa);
        configuracion = findViewById(R.id.conf);
        sos = findViewById(R.id.sos);
        invitacion = findViewById(R.id.invitar);
        bicicletas = findViewById(R.id.miBicicleta);
        contactos = findViewById(R.id.misContactos);

        Intent intent = getIntent();

        usuario = intent.getStringExtra("usuario");
        email = intent.getStringExtra("email");
        clave = intent.getStringExtra("clave");
        nombre = intent.getStringExtra("nombre");
        cel = intent.getStringExtra("celular");
        celular = cel;
        direccion = intent.getStringExtra("direccion");


        verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,Mapas.class);
                startActivity(intent);
            }
        });

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,Configuracion.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("nombre",nombre);
                intent.putExtra("direccion",direccion);
                intent.putExtra("email",email);
                intent.putExtra("clave",clave);
                intent.putExtra("celular",cel);
                startActivity(intent);
            }
        });

        bicicletas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,AgregarBicicleta.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("nombre",nombre);
                intent.putExtra("direccion",direccion);
                intent.putExtra("email",email);
                intent.putExtra("clave",clave);
                intent.putExtra("celular",cel);
                startActivity(intent);
            }
        });

        contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,Contactos.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("nombre",nombre);
                intent.putExtra("direccion",direccion);
                intent.putExtra("email",email);
                intent.putExtra("clave",clave);
                intent.putExtra("celular",cel);
                startActivity(intent);
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_CALL, Uri.parse("tel:3124142683"));
                Permisos.requestPermission(Inicio.this, Manifest.permission.READ_CONTACTS, "", Permisos_Call);
                if(ActivityCompat.checkSelfPermission(Inicio.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    startActivity(intent);
            }
        });

        invitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
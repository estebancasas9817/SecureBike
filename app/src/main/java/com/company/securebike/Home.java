package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button buttonConf = (Button)findViewById(R.id.b1);
        Button buttonCont = (Button)findViewById(R.id.b3);

        buttonConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent(v.getContext(),Configuracion.class);
                startActivity(intentWeb);
            }
        });
    }

    public void contactos(View v)
    {
        Toast.makeText(v.getContext(), "Tus contactos", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), Contactos.class);
        intent.putExtra("login", "Hola_4");
        startActivity(intent);
    }

    public void agregarBicicleta(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a agregar bicicleta", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), AgregarBicicleta.class);
        intent.putExtra("home", "Hola_5");
        startActivity(intent);
    }

    public void configurarBicicleta(View v)
    {
        Toast.makeText(v.getContext(), "Pasando a configurar bicicleta", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(v.getContext(), ConfiguracionBicicleta.class);
        intent.putExtra("home", "Hola_6");
        startActivity(intent);
    }
}
package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.securebike.auxiliares.ContactoAux;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class Home extends AppCompatActivity {

    int Permisos_Call = 0;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    private String telefono = new String();

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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

    public void llamar (View v)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Contactos");

        Query chequearUsuario = databaseReference.orderByChild("contacto");
        chequearUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setTelefono(snapshot.getChildren().iterator().next().getValue(ContactoAux.class).getNumero());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if(getTelefono().length() > 10)
        {
            try {
                int n = 3;
                String telaux = new String();
                while(n < getTelefono().length())
                {
                    telaux = telaux + getTelefono().charAt(n);
                    n++;
                }
                StringTokenizer linea = new StringTokenizer(telaux," ");
                String telDefinitivo = new String();

                while(linea.hasMoreTokens())
                {
                    telDefinitivo = telDefinitivo + linea.nextToken().toString();
                }

                System.out.println(telDefinitivo);
                setTelefono(telDefinitivo);

            }catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        Intent intent = new Intent (Intent.ACTION_CALL, Uri.parse("tel:"+getTelefono()));
        Permisos.requestPermission(this, Manifest.permission.CALL_PHONE, "", Permisos_Call);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

}
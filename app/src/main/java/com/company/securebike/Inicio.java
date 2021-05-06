package com.company.securebike;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.company.securebike.auxiliares.ContactoAux;
import com.company.securebike.auxiliares.MyDoes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class Inicio extends AppCompatActivity {

    ImageView verMapa,configuracion,sos,invitacion,bicicletas,contactos;
    int Permisos_Call = 0;
    DatabaseReference reference;
    String usuario;
    String email;
    String clave;
    String nombre;
    String cel;
    String celular;
    String direccion;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

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
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Contactos");

                reference = FirebaseDatabase.getInstance().getReference().child("Contactos").child(usuario);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        if (dataSnapshot.exists()) {

                            String tel = dataSnapshot.child("numero").getValue(String.class);

                            setTelefono(dataSnapshot.child(usuario).child("numero").getValue(String.class));

                            if(tel.length() > 10)
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

                            Intent intent = new Intent (Intent.ACTION_CALL, Uri.parse("tel:"+tel));
                            Permisos.requestPermission(Inicio.this, Manifest.permission.CALL_PHONE, "", Permisos_Call);
                            if(ActivityCompat.checkSelfPermission(Inicio.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(intent);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // set code to show an error
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    }
                });

//                Query chequearUsuario = databaseReference.orderByChild("aaa");
//                chequearUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            Toast.makeText(Inicio.this,"ENTRAAAAA",Toast.LENGTH_LONG).show();
////                            String tel = snapshot.child("numero").getValue(String.class);
//
//                            setTelefono(snapshot.getChildren().iterator().next().getValue(ContactoAux.class).getNumero());
//                            Toast.makeText(Inicio.this,getTelefono(),Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });



            }
        });

        invitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
                String aux = "https://www.youtube.com/watch?v=iuTtlb2COtc&ab_channel=CamiloVEVO";
                intent.putExtra(Intent.EXTRA_TEXT,aux);
                startActivity(intent);

            }
        });
    }



    private String telefono = new String();

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
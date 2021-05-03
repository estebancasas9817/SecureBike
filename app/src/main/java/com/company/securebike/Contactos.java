package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.company.securebike.auxiliares.ContactoAux;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.Query;

public class Contactos extends AppCompatActivity {

    int Permisos_Read_Contact = 0;
    String[] mProjection;
    Cursor cursor;

    private static final int PICK_CONTACT_INDEX = 1;
    EditText Cnombre, Cnumero;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    static private String nombreContacto;
    static private String numeroContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        Permisos.requestPermission(this, Manifest.permission.READ_CONTACTS, "", Permisos_Read_Contact);
        mProjection = new String[]{ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY};
        Cnombre = findViewById(R.id.etNombre);
        Cnumero = findViewById(R.id.etNumero);

        initView();
        seleccionarContacto();
    }

    private void initView()
    {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permiso == PackageManager.PERMISSION_GRANTED)
        {
            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
        }
    }

    public void seleccionarContacto ()
    {

        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_INDEX);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT_INDEX)
        {
            if(resultCode == RESULT_OK)
            {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    String nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String numero = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Cnombre.setText(nombre);
                    Cnumero.setText(numero);

                    nombreContacto = new String(nombre);
                    numeroContacto = new String(numero);
                }

            }
        }
    }

    public void agregar(final View v)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Contactos");

        Query chequearUsuario =databaseReference.orderByChild("contacto");
        chequearUsuario.addListenerForSingleValueEvent(new ValueEventListener()
                                                       {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                               if (snapshot.exists())
                                                               {
                                                                   Toast.makeText(v.getContext(), "No se puede tener más de un contacto SOS", Toast.LENGTH_LONG).show();
                                                               } else {

                                                                   ContactoAux contactoAux = new ContactoAux(nombreContacto, numeroContacto);
                                                                   databaseReference.child(nombreContacto).setValue(contactoAux);
                                                                   Intent intent = new Intent(v.getContext(), MapsActivity.class);
                                                                   startActivity(intent);
                                                               }
                                                           }
                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                           }
                                                       });

    }

    public void cambiar(final View v)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Contactos");

        Query chequearUsuario =databaseReference.orderByChild("contacto");
        chequearUsuario.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    databaseReference.removeValue();

                    ContactoAux contactoAux = new ContactoAux(nombreContacto, numeroContacto);
                    databaseReference.child(nombreContacto).setValue(contactoAux);
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

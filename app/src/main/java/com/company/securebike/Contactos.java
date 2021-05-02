package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

public class Contactos extends AppCompatActivity {

    int Permisos_Read_Contact = 0;
    String[] mProjection;
    Cursor cursor;
    ContactsAdapter CA;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        Permisos.requestPermission(this, Manifest.permission.READ_CONTACTS, "", Permisos_Read_Contact);

        lista = (ListView)findViewById(R.id.listaContactos);
        mProjection = new String[]{ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY};
        CA = new ContactsAdapter(this, null, 0);
        lista.setAdapter(CA);

        initView();
    }

    private void initView()
    {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permiso == PackageManager.PERMISSION_GRANTED)
        {
            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
            CA.changeCursor(cursor);
        }

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

package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.company.securebike.auxiliares.MyDoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditTaskDesk extends AppCompatActivity {

    EditText titleDoes, descDoes, dateDoes,editarNombre,editarRasgos;
    Button btnSaveUpdate, btnDelete;
    DatabaseReference reference;
    String usuario;
    ImageView imagen;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri fotoUri;
    Boolean bandera = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);
        titleDoes = findViewById(R.id.titleDoes);
        descDoes = findViewById(R.id.descDoes);
        editarNombre = findViewById(R.id.editarNombre);
        dateDoes = findViewById(R.id.dateDoes);
        imagen = findViewById(R.id.editarFoto);
        editarRasgos = findViewById(R.id.editarRasgos);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");

        String url = intent.getStringExtra("foto");

        titleDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));
        editarNombre.setText(getIntent().getStringExtra("nombre"));
        editarRasgos.setText(getIntent().getStringExtra("rasgos"));
        Picasso.get().load(url).into(imagen);

        final String keykeyDoes = getIntent().getStringExtra("keydoes");



        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String [] permiso = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permiso,1001);
                    }
                    else{
                        escogerImagenGaleria();
                        bandera = true;

                    }
                }
                else {
                    escogerImagenGaleria();
                    bandera = true;
                }

            }
        });


        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario);




                String titulo = titleDoes.getText().toString();
                String descrip = descDoes.getText().toString();
                String fechas = dateDoes.getText().toString();
                String key = keykeyDoes;
                String username = usuario;
                String nombre = editarNombre.getText().toString();
                String rasgos = editarRasgos.getText().toString();
                reference.child("Does" + key).child("color").setValue(titleDoes.getText().toString());
                reference.child("Does" + key).child("marca").setValue(descDoes.getText().toString());
                reference.child("Does" + key).child("matricula").setValue(dateDoes.getText().toString());
                reference.child("Does" + key).child("keydoes").setValue(key);
                reference.child("Does" + key).child("nombre").setValue(editarNombre.getText().toString());
                reference.child("Does" + key).child("rasgos").setValue(editarRasgos.getText().toString());

                if(bandera == true) {
                    AgregarFirebase(titulo,descrip,fechas,key,username,nombre,rasgos,fotoUri);

                }
//
                Intent a = new Intent(EditTaskDesk.this,AgregarBicicleta.class);
                a.putExtra("usuario",usuario);


                Toast.makeText(EditTaskDesk.this,"Cambios guardados...",Toast.LENGTH_LONG).show();
                startActivity(a);



//
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario).child("Does" + keykeyDoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent a = new Intent(EditTaskDesk.this,AgregarBicicleta.class);
                            a.putExtra("usuario",usuario);
                            Toast.makeText(EditTaskDesk.this,"Bicicleta eliminada...",Toast.LENGTH_LONG).show();
                            startActivity(a);
                        }
                    }
                });

            }
        });
    }

    private void AgregarFirebase(final String titulo, final String descrip, final String fechas, final String key, final String username, final String nombre, final String rasgos, Uri uri){

        final StorageReference archivo = storageReference.child(username).child("Does"+key);


//        MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos,uri.toString());
//
//        reference.child("Does" + doesNum).setValue(myDoesAux);
//                        progressBar.setVisibility(View.INVISIBLE);
//        Toast.makeText(NewTaskAct.this,"Añadido satisfactoriamente....",Toast.LENGTH_LONG).show();

        archivo.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(EditTaskDesk.this,"ENTRAAAA",Toast.LENGTH_LONG).show();
                archivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        reference.child("Does" + key).child("foto").setValue(uri.toString());

//                        MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos,uri.toString());

//                        reference.child("Does" + key).setValue(myDoesAux);
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditTaskDesk.this,"Editado satisfactoriamente....",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditTaskDesk.this,"Error....",Toast.LENGTH_LONG).show();
            }
        });
    }
    private String getFileExt(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }


    private void escogerImagenGaleria() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1000);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    escogerImagenGaleria();

                }
                else{
                    Toast.makeText(EditTaskDesk.this,"Permiso denegado....",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {

            fotoUri = data.getData();
            imagen.setImageURI(fotoUri);
        }
        else if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();

            Bitmap foto = (Bitmap) bundle.get("data");
            imagen.setImageBitmap(foto);
        }
    }
}
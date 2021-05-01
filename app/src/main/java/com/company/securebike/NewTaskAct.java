package com.company.securebike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.company.securebike.auxiliares.MyDoes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class NewTaskAct extends AppCompatActivity {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText titledoes, descdoes, datedoes,rasgosDoes,nombreDoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    Integer doesNum = new Random().nextInt();
    String keydoes = Integer.toString(doesNum);
    String usuario;
//     Button btnImg;
    private Uri imagenUri;

    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ImageView imagen;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);
        rasgosDoes = findViewById(R.id.rasgosDoes);
        nombreDoes = findViewById(R.id.nombreDoes);
        imagen = findViewById(R.id.addFoto);
//        btnImg = findViewById(R.id.btnImg);
        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

//        progressBar.setVisibility(View.INVISIBLE);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");


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

                    }
                }
                else {
                    escogerImagenGaleria();
                }
            }
        });



        btnSaveTask.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                 insert data to database
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(usuario);
                String titulo = titledoes.getText().toString();
                String descrip = descdoes.getText().toString();
                String fechas = datedoes.getText().toString();
                String key = keydoes;
                String username = usuario;
                String nombre = nombreDoes.getText().toString();
                String rasgos = rasgosDoes.getText().toString();

                if(imagenUri != null) {
                    AgregarFirebase(titulo,descrip,fechas,key,username,nombre,rasgos,imagenUri);

                }else{
                    Toast.makeText(NewTaskAct.this,"Porfa Selecciona una imagen...",Toast.LENGTH_LONG).show();
                }
//                MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos);

//                reference.child("Does" + doesNum).setValue(myDoesAux);

//                Intent ir = new Intent(NewTaskAct.this,AgregarBicicleta.class);
//                ir.putExtra("usuario",usuario);
//                Toast.makeText(NewTaskAct.this,"Creado con exito....",Toast.LENGTH_SHORT).show();
//                startActivity(ir);

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devolver = new Intent(NewTaskAct.this,AgregarBicicleta.class);
                devolver.putExtra("usuario",usuario);
                startActivity(devolver);
            }
        });


    }

    private void AgregarFirebase(final String titulo, final String descrip, final String fechas, final String key, final String username, final String nombre, final String rasgos, Uri uri){

        final StorageReference archivo = storageReference.child(System.currentTimeMillis() + "." + getFileExt(uri));
//        MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos,uri.toString());
//
//        reference.child("Does" + doesNum).setValue(myDoesAux);
//                        progressBar.setVisibility(View.INVISIBLE);
//        Toast.makeText(NewTaskAct.this,"Añadido satisfactoriamente....",Toast.LENGTH_LONG).show();
        archivo.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewTaskAct.this,"ENTRAAAA",Toast.LENGTH_LONG).show();
                archivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        MyDoes myDoesAux = new MyDoes(titulo,descrip,fechas,key,username,nombre,rasgos,uri.toString());

                        reference.child("Does" + doesNum).setValue(myDoesAux);
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(NewTaskAct.this,"Añadido satisfactoriamente....",Toast.LENGTH_LONG).show();
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
                Toast.makeText(NewTaskAct.this,"Error....",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(NewTaskAct.this,"Permiso denegado....",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {

            imagenUri = data.getData();
            imagen.setImageURI(imagenUri);
        }
        else if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();

            Bitmap foto = (Bitmap) bundle.get("data");
            imagen.setImageBitmap(foto);
        }
    }
}
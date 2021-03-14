package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Configuracion extends AppCompatActivity {

    TextView seleccionarText;
    ArrayList<String> arrayList;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        seleccionarText = findViewById(R.id.selecionarTextView);

        arrayList = new ArrayList<>();

        arrayList.add("calle 152 #12c-30");
        arrayList.add("calle 149 #27-12");
        arrayList.add("calle 98 #7-24");
        arrayList.add("calle 45 #7-13");

        seleccionarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Configuracion.this);
                dialog.setContentView(R.layout.dialog_search);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                EditText editDireccion = dialog.findViewById(R.id.editDirecion);
                ListView lista = dialog.findViewById(R.id.lista);


                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Configuracion.this, android.R.layout.simple_list_item_1,arrayList);
                lista.setAdapter(adapter);

                editDireccion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        seleccionarText.setText(adapter.getItem(position));

                        dialog.dismiss();
                    }
                });

            }
        });
    }
}
package com.company.securebike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
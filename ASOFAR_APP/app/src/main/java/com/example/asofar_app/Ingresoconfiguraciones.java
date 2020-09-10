package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Ingresoconfiguraciones extends AppCompatActivity {

    EditText contraseña;
    ImageView validaconfig, regresamenu;
    private String contraseñastrandar = "0852";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresoconfiguraciones);


        contraseña= (EditText) findViewById(R.id.txticontraseña);
        validaconfig = (ImageView) findViewById(R.id.imabtnvalida);
        regresamenu = (ImageView) findViewById(R.id.imagenregresa);

        validaconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contraseña.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),"CAMPO VACIO",Toast.LENGTH_LONG).show();

                }else{

                    if(contraseña.getText().toString().equals(contraseñastrandar)){

                        //Toast.makeText(getApplicationContext(),"CORRECTO",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Ingresoconfiguraciones.this, configuracionessqlite.class);
                        startActivity(intent);
                        finish();
                    }else{

                        Toast.makeText(getApplicationContext(),"CONTRASEÑA INCORRECTA",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        regresamenu.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                 Intent intentregreso = new Intent(Ingresoconfiguraciones.this, Opcionesingreso.class);
                 startActivity(intentregreso);
                 finish();
             }
        });
    }
}
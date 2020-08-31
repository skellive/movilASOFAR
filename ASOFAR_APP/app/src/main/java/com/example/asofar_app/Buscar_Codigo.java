package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Buscar_Codigo extends AppCompatActivity implements  View.OnClickListener{

    Button btnmodulocaptacodigo, btngeneracodigobusqueda,btnlanzaconsulta;
    EditText txtmuestracodigoconsulta,txtmuestracodigoconsulta2,txtmuestraidconsulta,txtmuestrafecharegistroconsulta;

    //objeto de mi clase SQLite
    ConexionSQLiteHelper conn;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar__codigo);

        // Llamar a la conexion
        conn = new ConexionSQLiteHelper(getApplicationContext(), "BD_ASOFAR_APP", null, 1);


        //Inicializa objetos
        txtmuestracodigoconsulta = (EditText) findViewById(R.id.txtcodigobusqueda);
        txtmuestraidconsulta = (EditText) findViewById(R.id.txtmustraid);
        txtmuestracodigoconsulta2 = (EditText) findViewById(R.id.txtmuestracodigo);
        txtmuestrafecharegistroconsulta = (EditText) findViewById(R.id.txtmuestrafecharegistro);

        btnmodulocaptacodigo =  (Button) findViewById(R.id.btnregresaprincipal);
        btngeneracodigobusqueda = (Button) findViewById(R.id.bntcaptacodigobusqueda);
        btnlanzaconsulta = (Button) findViewById(R.id.btnbucarcodigo);


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        btngeneracodigobusqueda.setOnClickListener(this);

        //boton que te envia al modulo de capta codigo
        btnmodulocaptacodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modulocaptacodigo();
            }
        });

        //Boton genera la busqueda por base y nos retorna si esta insertado con sus datos y fecha o si no hay registro como tal
        btnlanzaconsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzaconsulta();
            }
        });
    }


    //----------------------------------AREA DE METODOS-----------------------------


    //Obtener los resultados del escaneo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                txtmuestracodigoconsulta.setText("");
                Toast.makeText(this, "No hay Codigo Scanneado!!", Toast.LENGTH_LONG).show();
            } else {
                txtmuestracodigoconsulta.setText("");
                // Toast.makeText(this, "Hola"+ result.getContents(), Toast.LENGTH_LONG).show();
                txtmuestracodigoconsulta.setText(result.getContents());




            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }

    // metodo que te envia al modulo de capta codigo
    public void modulocaptacodigo(){

        Intent modulocaptacodigo = new Intent(this,MainActivity.class);
        startActivity(modulocaptacodigo);


    };
    //busqueda por base, si existe o no el registro del codigo
    public void lanzaconsulta(){

        if(txtmuestracodigoconsulta.getText().toString().isEmpty()){

            Toast.makeText(this, "Campo Vacio, Por Favor Scannea un Codigo!", Toast.LENGTH_LONG).show();

        }else {
            // Generamos Objeto
            SQLiteDatabase db = conn.getWritableDatabase();

            //parametro a realizar la consulta;
            String[] parametro = {txtmuestracodigoconsulta.getText().toString()};
            String[] campos = {"ID_COD", "COD_BARRAS", "FECHA_CREACION"};

            try {


                // Realizamos la consulta
                Cursor cursor = db.query("COD_GENERADOS", campos, "COD_BARRAS=?", parametro, null, null, null);
                // que me separe los datos que lanze en mi consulta
                cursor.moveToFirst();

                // asignacion de datos de la consulta
                txtmuestraidconsulta.setText(cursor.getString(0));
                txtmuestracodigoconsulta2.setText(cursor.getString(1));
                txtmuestrafecharegistroconsulta.setText(cursor.getString(2));
                cursor.close();

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "No existe el codigo , Por favor registrelo y vuelva a realizar el proceso de busqueda", Toast.LENGTH_SHORT).show();

            };

        }

    };
}
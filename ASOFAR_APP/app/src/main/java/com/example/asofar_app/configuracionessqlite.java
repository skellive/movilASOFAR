package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class configuracionessqlite extends AppCompatActivity {




    public  EditText txtparametro,txtidcajadb,txtipserver,txtpuertodb;
    Button btnbuscaregistro,btnactualizaregistro;

    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracionessqlite);

        //txtparametro = (EditText) findViewById(R.id.txtbuscaidcaja);
        txtidcajadb = (EditText) findViewById(R.id.txtidcajadbb);
        txtipserver = (EditText) findViewById(R.id.txtipserver);
        txtpuertodb = (EditText) findViewById(R.id.txtpuertodb);

       // btnbuscaregistro= (Button) findViewById(R.id.bntbuscaiddb);
        btnactualizaregistro= (Button) findViewById(R.id.btnactualizaws);

        consultar();



        btnactualizaregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });



    }

    public void consultar(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();

            txtidcajadb.setText(cursor.getString(1));
            txtipserver.setText(cursor.getString(2));
            txtpuertodb.setText(cursor.getString(3));
           /* idprueba = cursor.getString(1);
            ipwsprueba= cursor.getString(2);
            puertoprueba = cursor.getString(3);*/

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }

    public void actualizar(){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String codactualiza = "1";
        String[] parametros = {codactualiza};
        ContentValues values = new ContentValues();
        values.put("cajadb",txtidcajadb.getText().toString());
        values.put("ipserverdb",txtipserver.getText().toString());
        values.put("puertodb",txtpuertodb.getText().toString());

        db.update("config", values, "iddb=?",parametros);
        Toast.makeText(getApplicationContext()," SE ACTUALIZO EL REGISTRO",Toast.LENGTH_LONG).show();

        db.close();


    }
}
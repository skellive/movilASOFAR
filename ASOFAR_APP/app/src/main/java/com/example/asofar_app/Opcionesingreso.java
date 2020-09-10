package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Opcionesingreso extends AppCompatActivity {

    ImageView imagenbtnmantenimientos, imagenbtngeneraproducto;


   public String  idprueba ;
    public String  ipwsprueba;
    public String puertoprueba;
    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcionesingreso);

        imagenbtnmantenimientos = (ImageView) findViewById(R.id.imagetipo);
        imagenbtngeneraproducto = (ImageView) findViewById(R.id.imageproducto);

        consultarconfig();

        imagenbtnmantenimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generamantenimiento();
            }
        });

        imagenbtngeneraproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generaproducto();
            }
        });

    }

    public void generamantenimiento(){

        final CharSequence [] opcionesmantenimiento ={"+NUEVO TIPO", "+NUEVA MEDIDA", "+NUEVA CATEGORIA","+NUEVA MARCA" , "+NUEVA PRESENTACION"};
        final AlertDialog.Builder aleBuilder = new AlertDialog.Builder(Opcionesingreso.this);
        aleBuilder.setTitle("SELECCIONE UNA OPCION: ");
        aleBuilder.setItems(opcionesmantenimiento, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opcionesmantenimiento[i].equals("+NUEVO TIPO")){

                    Intent intent = new Intent(Opcionesingreso.this, Mantenimientotipo.class);
                    startActivity(intent);
                    finish();
                }else if (opcionesmantenimiento[i].equals("+NUEVA MEDIDA")){

                    Intent intent2 = new Intent(Opcionesingreso.this, Mantenimientomedida.class);
                    startActivity(intent2);
                    finish();

                   //

                }else if (opcionesmantenimiento[i].equals("+NUEVA CATEGORIA")){

                    Intent intent3 = new Intent(Opcionesingreso.this, Mantenimientoenvase.class);
                    startActivity(intent3);
                    finish();

                }else if(opcionesmantenimiento[i].equals("+NUEVA MARCA")){
                    Intent intent4 = new Intent(Opcionesingreso.this, Mantenimientomarcas.class);
                    startActivity(intent4);
                    finish();

                }else if(opcionesmantenimiento[i].equals("+NUEVA PRESENTACION")){

                    Intent intent5 = new Intent(Opcionesingreso.this, Mantenimientopresentacion.class);
                    startActivity(intent5);
                    finish();

                }else {

                    dialog.dismiss();

                }
            }
        });
            aleBuilder.show();
    }

    public void generaproducto(){

        final CharSequence [] opcionesproducto ={"+INGRESO DE PRODUCTOS", "CONFIGURACIONES","SALIR DEL SISTEMA"};
        final AlertDialog.Builder aleBuilder = new AlertDialog.Builder(Opcionesingreso.this);
        aleBuilder.setTitle("SELECCIONE UNA OPCION: ");
        aleBuilder.setItems(opcionesproducto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opcionesproducto[i].equals("+INGRESO DE PRODUCTOS")){

                    Intent iconfig = new Intent(Opcionesingreso.this, MainActivity.class);

                    startActivity(iconfig);
                    finish();


                }else if(opcionesproducto[i].equals("CONFIGURACIONES")){

                    Intent inte = new Intent(Opcionesingreso.this, Ingresoconfiguraciones.class);
                    startActivity(inte);
                    finish();
                }else {

                    dialog.dismiss();

                }
            }
        });
        aleBuilder.show();
    }
    public void consultarconfig(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();


           idprueba = cursor.getString(1);
            ipwsprueba= cursor.getString(2);
            puertoprueba = cursor.getString(3);

            Toast.makeText(getApplicationContext(),""+idprueba+"-"+ipwsprueba+"-"+puertoprueba,Toast.LENGTH_LONG).show();

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }
}
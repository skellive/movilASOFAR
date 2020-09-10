package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asofar_app.utilidades.utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Mantenimientomarcas extends AppCompatActivity {
    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);
    public String  idpruebamm ;
    public String  ipwspruebamm;
    public String puertopruebamm;


    EditText txtmarcacapta;
    Button btnguardamarca;
    TextView validamarca, regresamenumarca;
    Spinner spinnerestadovalidamarca;
    public String vestadomarca = "";
    public String idmarca = "";

    ProgressBar progresomarca;
    Handler handler = new Handler();
    boolean isactivomarca = false;
    int i = 0;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimientomarcas);




        txtmarcacapta =(EditText) findViewById(R.id.txtmarcasmantenimiento);
        validamarca = (TextView) findViewById(R.id.txtbtnverificamanatenimiento);
        spinnerestadovalidamarca = (Spinner) findViewById(R.id.spinnermarcasmantenimiento);
        btnguardamarca = (Button)  findViewById(R.id.btngenerarmarcamantenimiento);
        regresamenumarca = (TextView)  findViewById(R.id.txtbtnregresaaopcionesmarca);
        progresomarca = (ProgressBar) findViewById(R.id.progressBarmarca);

        consultarconfigmarcas();

        spinnerestadovalidamarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vestadomarca = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),vestadomarca,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        validamarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultacodigoexistenciamarca("http://"+ipwspruebamm+":"+puertopruebamm+"/asofar_app/Mantenimientos/consulta_marca.php?nombremarca="+txtmarcacapta.getText()+"");
            }
        });



        btnguardamarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnguardamarca.setEnabled(false);
                insertamarca("http://"+ipwspruebamm+":"+puertopruebamm+"/asofar_app/Mantenimientos/inserta_marca.php");

                if (!isactivomarca){

                    Thread hilo = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (i<=50){

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresomarca.setProgress(i);

                                    }
                                });

                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(i==50){
                                    //  Toast.makeText(getApplicationContext(), "------------------", Toast.LENGTH_LONG).show();
                                    validainserciononlinemarca("http://"+ipwspruebamm+":"+puertopruebamm+"/asofar_app/Mantenimientos/consulta_marcavalida.php?nombremarcavalida="+txtmarcacapta.getText().toString()+"");

                                    Intent intentT = new Intent(Mantenimientomarcas.this, Opcionesingreso.class);
                                    startActivity(intentT);
                                    finish();



                                }
                                i++;
                                // isactivo = true;
                            }

                        }

                    });
                    hilo.start();
                }
            }
        });




        regresamenumarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentenvase= new Intent(Mantenimientomarcas.this, Opcionesingreso.class);
                startActivity(intentenvase);
                finish();
            }
        });

    }


    public void consultacodigoexistenciamarca(String RUTA){

        if (txtmarcacapta.getText().toString().isEmpty()) {

            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_LONG).show();
        } else {
            String evasess = null;

            evasess= txtmarcacapta.getText().toString();


            //---------------------------------------------realizar consulta del codigo-----------------------------------------------------

            // JsonArrayRequest  realizamos el tipo de llamada que se  que es GET
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RUTA, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //2. declaramos un objeto de tipo  JSONObject
                    JSONObject jsonObject = null;
                    //3.declaramos un bucle for para recorrer los datos ws
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            jsonObject = response.getJSONObject(i);
                            //asiganamos los datos ws a cada uno de los controles por medio de los nombre de las tablas


                            //------------------------------------------------------------------
                            //txtnombre.setText(jsonObject.getString("nombre_prueba_app"));
                            //Toast.makeText(getApplicationContext(), "CODIGO EXISTE", Toast.LENGTH_SHORT).show();

                            idmarca = (jsonObject.getString("id_marcas"));////////////////////////////////////////////////////////////////////////////////////


                            Toast.makeText(getApplicationContext(), "CODIGO EXISTE: "+idmarca, Toast.LENGTH_SHORT).show();
                            btnguardamarca.setEnabled(false);

                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                        }
                    }
                }

                // Generar un metodo que alerte el error
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "CODIGO NO REGISTRADO [ZONA VALIDA MARCA]", Toast.LENGTH_SHORT).show();//error.getMessage()
                    btnguardamarca.setEnabled(true);

                }
            });

            // ahora comiamos el servicio y lo pegamos donde inicializamos los onjetos osea arriba --  RequestQueue requestQueue

            //
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);



          /*
          // Llamar a la conexion----------- en caso de guaradar local la info
          ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "BD_ASOFAR_APP", null, 1);
          // Generamos Objeto
          SQLiteDatabase db = conn.getWritableDatabase();

          String insert = " INSERT INTO  COD_GENERADOS(COD_BARRAS) VALUES('" + codigo + "')";

          db.execSQL(insert);


             db.close();*/
        }

    }

    public void insertamarca(String RUTA){

        if (txtmarcacapta.getText().toString().isEmpty() ) {

            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
        } else {

            // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RUTA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), "VERIFICA.....", Toast.LENGTH_SHORT).show();



                }

                //2-  Generar el metodo de error con una ', new reponse.error'
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "ERROR AL GENERAR, REVISE LA CONEXION!", Toast.LENGTH_SHORT).show();
                }
                // 3- Generar el metodo getParams pero el que indice el metodo a utilizar post o get , en nuestro caso post -- ate de eso ponemos doble parentesis
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    // 4. haremos uso del metodo MAP
                    Map<String, String> parametros = new HashMap<String, String>();

                    //5. en el metodo put enviamos la info

                    parametros.put("nombre_marca", txtmarcacapta.getText().toString());//////////////////////////////////
                    parametros.put("estado_marca", vestadomarca);



                    //retornamos los parametros creados es decir reemplazamos
                    //return super.getParams(); por return parametros

                    return parametros;

                }

                //6.  ponemos el punto y coma
            };
            // 7.hacemos uso del objeto request procesamos la operacion

            requestQueue = Volley.newRequestQueue(this);

            // 8. enviamos la solicitud de la operacion
            requestQueue.add(stringRequest);

        }



    }

    public void validainserciononlinemarca(String RUTA){


        String tipoovalida = null;

        tipoovalida = txtmarcacapta.getText().toString();


        //---------------------------------------------realizar consulta del codigo-----------------------------------------------------

        // JsonArrayRequest  realizamos el tipo de llamada que se  que es GET
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RUTA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //2. declaramos un objeto de tipo  JSONObject
                JSONObject jsonObject = null;
                //3.declaramos un bucle for para recorrer los datos ws
                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        //asiganamos los datos ws a cada uno de los controles por medio de los nombre de las tablas


                        //------------------------------------------------------------------
                        //txtnombre.setText(jsonObject.getString("nombre_prueba_app"));
                        //Toast.makeText(getApplicationContext(), "CODIGO EXISTE", Toast.LENGTH_SHORT).show();

                        idmarca = (jsonObject.getString("id_marcas"));



                        Toast.makeText(getApplicationContext(), "SE REGISTRO UN NUEVO TIPO!!: "+idmarca, Toast.LENGTH_SHORT).show();
                        btnguardamarca.setEnabled(false);
                        txtmarcacapta.setText("");
                        isactivomarca = false;


                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                    }
                }
            }

            // Generar un metodo que alerte el error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                Toast.makeText(getApplicationContext(), "TIPO NO REGISTRADO,REVISE SU CONEXION WS [ZONA]", Toast.LENGTH_SHORT).show();//error.getMessage()
                btnguardamarca.setEnabled(true);

            }
        });

        // ahora comiamos el servicio y lo pegamos donde inicializamos los onjetos osea arriba --  RequestQueue requestQueue

        //
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



          /*
          // Llamar a la conexion----------- en caso de guaradar local la info
          ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "BD_ASOFAR_APP", null, 1);
          // Generamos Objeto
          SQLiteDatabase db = conn.getWritableDatabase();

          String insert = " INSERT INTO  COD_GENERADOS(COD_BARRAS) VALUES('" + codigo + "')";

          db.execSQL(insert);


             db.close();*/



    }

    public void consultarconfigmarcas(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();


            idpruebamm = cursor.getString(1);
            ipwspruebamm= cursor.getString(2);
            puertopruebamm = cursor.getString(3);

           // Toast.makeText(getApplicationContext(),""+idprueba+"-"+ipwsprueba+"-"+puertoprueba,Toast.LENGTH_LONG).show();

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }
}
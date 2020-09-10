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

public class Mantenimientomedida extends AppCompatActivity {

    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);
    public String  idpruebamme ;
    public String  ipwspruebamme;
    public String puertopruebamme;

    EditText txtmedidacapta;
    Button btnguardamedida;
    TextView validamedidass, regresamenumedidas;
    Spinner spinnerestadomedidavalida;
    public String vestadomedida = "";
    public String idmedida = "";


    ProgressBar progresomedidas;
    Handler handlerm = new Handler();
    boolean isactivomedidas = false;
   private int i = 0;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimientomedida);


        txtmedidacapta =(EditText) findViewById(R.id.txtmedidamantenimiento);
        validamedidass = (TextView) findViewById(R.id.txtbtnverificamedida);
        spinnerestadomedidavalida = (Spinner) findViewById(R.id.spinnermdidamantenimiento);
        btnguardamedida = (Button)  findViewById(R.id.btngenerarmedidamantenimiento);
        regresamenumedidas = (TextView)  findViewById(R.id.txtbtnregresaaopcionesmedida);
        progresomedidas = (ProgressBar) findViewById(R.id.progressBarmedidas);



        consultarconfigmedida();





        spinnerestadomedidavalida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vestadomedida = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),vestadomedida,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         validamedidass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultacodigoexistenciamedida("http://"+ipwspruebamme+":"+puertopruebamme+"/asofar_app/Mantenimientos/consulta_medidas.php?nombremedida="+txtmedidacapta.getText()+"");
            }
        });



        btnguardamedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnguardamedida.setEnabled(false);
                insertamedida("http://"+ipwspruebamme+":"+puertopruebamme+"/asofar_app/Mantenimientos/inserta_medidas.php");

                if (!isactivomedidas){

                    Thread hilo = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (i<=50){

                                handlerm.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progresomedidas.setProgress(i);

                                    }
                                });

                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(i==50){
                                    //  Toast.makeText(getApplicationContext(), "------------------", Toast.LENGTH_LONG).show();
                                    validainserciononlinemedida("http://"+ ipwspruebamme+":"+puertopruebamme+"/asofar_app/Mantenimientos/consulta_medidasvalida.php?nombremedidavalida="+txtmedidacapta.getText().toString()+"");

                                    Intent intentT = new Intent(Mantenimientomedida.this, Opcionesingreso.class);
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




        regresamenumedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentmedida= new Intent(Mantenimientomedida.this, Opcionesingreso.class);
                startActivity(intentmedida);
                finish();
            }
        });



    }


    public void consultacodigoexistenciamedida(String RUTA){

        if (txtmedidacapta.getText().toString().isEmpty()) {

            Toast.makeText(this, "Campo Vacio", Toast.LENGTH_LONG).show();
        } else {
            String medidass = null;

            medidass= txtmedidacapta.getText().toString();


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

                            idmedida = (jsonObject.getString("id_medidas"));////////////////////////////////////////////////////////////////////////////////////


                            Toast.makeText(getApplicationContext(), "CODIGO EXISTE: "+idmedida, Toast.LENGTH_SHORT).show();
                            btnguardamedida.setEnabled(false);

                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                        }
                    }
                }

                // Generar un metodo que alerte el error
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "CODIGO NO REGISTRADO [ZONA VALIDA MEDIDAS]", Toast.LENGTH_SHORT).show();//error.getMessage()
                    btnguardamedida.setEnabled(true);

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

    public void insertamedida(String RUTA){

        if (txtmedidacapta.getText().toString().isEmpty() ) {

            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
        } else {

            // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RUTA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), "VERIFICANDO....", Toast.LENGTH_SHORT).show();

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

                    parametros.put("nombre_medidas", txtmedidacapta.getText().toString());//////////////////////////////////
                    parametros.put("estado_medidas", vestadomedida);



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

    public void validainserciononlinemedida(String RUTA){


        String medidaovalida = null;

        medidaovalida= txtmedidacapta.getText().toString();


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

                        idmedida = (jsonObject.getString("id_medidas"));



                        Toast.makeText(getApplicationContext(), "SE REGISTRO UN NUEVO MEDIDA!!: "+idmedida, Toast.LENGTH_SHORT).show();
                        btnguardamedida.setEnabled(false);
                        txtmedidacapta.setText("");
                        isactivomedidas = false;


                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                    }
                }
            }

            // Generar un metodo que alerte el error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                Toast.makeText(getApplicationContext(), "MEDIDA NO REGISTRADO,REVISE SU CONEXION WS [ZONA]", Toast.LENGTH_SHORT).show();//error.getMessage()
                btnguardamedida.setEnabled(true);

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
    public void consultarconfigmedida(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();


            idpruebamme = cursor.getString(1);
            ipwspruebamme= cursor.getString(2);
            puertopruebamme = cursor.getString(3);

            // Toast.makeText(getApplicationContext(),""+idprueba+"-"+ipwsprueba+"-"+puertoprueba,Toast.LENGTH_LONG).show();

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }
}
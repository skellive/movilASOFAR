package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class insertaproducto extends AppCompatActivity {

    Button btnsync, btnverifica;
    EditText txtnombre, txtbusqueda;

    // aqui lo pegamos ajjajajajjaa
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertaproducto);

        btnsync = (Button) findViewById(R.id.btnguardamysql);
        btnverifica = (Button) findViewById(R.id.btnverificamysql);
        txtnombre = (EditText) findViewById(R.id.txtpruebanombresync);
        txtbusqueda = (EditText) findViewById(R.id.txtbusquedamysql);




        btnsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               guardaenmysqllosdatos("http://192.168.100.15:8080/asofar_app/inserta_producto.php");
            }
        });

        btnverifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaexistencia("http://192.168.100.15:8080/asofar_app/consulta_producto.php?nombre="+txtbusqueda.getText()+"");
            }
        });
    }


    private void guardaenmysqllosdatos(String URL){

        // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }

            //2-  Generar el metodo de error con una ', new reponse.error'
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR AL GENERAR, REVISE LA CONEXION!", Toast.LENGTH_SHORT).show();
            }
            // 3- Generar el metodo getParams pero el que indice el metodo a utilizar post o get , en nuestro caso post -- ate de eso ponemos doble parentesis
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // 4. haremos uso del metodo MAP
                Map<String,String> parametros = new HashMap<String,String>();

                //5. en el metodo put enviamos la info

                parametros.put("nombre_prueba_app",txtnombre.getText().toString());

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


    //-----------------------------------------------------------------------------------------------------------------------------

    private void verificaexistencia(String RUTA){
        // JsonArrayRequest  realizamos el tipo de llamada que se  que es GET
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RUTA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //2. declaramos un objeto de tipo  JSONObject
                JSONObject jsonObject= null;
                //3.declaramos un bucle for para recorrer los datos ws
                for(int i=0;i<response.length(); i++){

                    try{
                        jsonObject = response.getJSONObject(i);
                        //asiganamos los datos ws a cada uno de los controles por medio de los nombre de las tablas


                        //------------------------------------------------------------------
                        txtnombre.setText(jsonObject.getString("nombre_prueba_app"));


                    }catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Generar un metodo que alerte el error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // ahora comiamos el servicio y lo pegamos donde inicializamos los onjetos osea arriba --  RequestQueue requestQueue

        //
        requestQueue =Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
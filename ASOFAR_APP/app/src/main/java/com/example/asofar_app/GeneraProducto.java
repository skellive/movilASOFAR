package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asofar_app.entidades.envase;
import com.example.asofar_app.entidades.marcas;
import com.example.asofar_app.entidades.medidas;
import com.example.asofar_app.entidades.presentacion;
import com.example.asofar_app.entidades.tipo;
import com.example.asofar_app.utilidades.utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneraProducto extends AppCompatActivity {


    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);

    public String cajabdp = null;
    public String ipbdp = null;
    public String puertobdp = null;

    TextView txtcodigo, txtcaja,txtnombre,txtdescripcion, txtidcodigo;
    EditText txtmppeso,txtmpcantidad,txtmpunidades,txtmpprecio;
    Button btnmgeneraproductototal,btnregresaractividadp;





    Spinner spinnertipo, spinnermedidas,spinnerenvase, spinnermarcas,spinnerestado,spinneriva, spinnerreceta,spinnerpresentacion ;

    final List<tipo> tipos = new ArrayList<>();
    final List<medidas> medida = new ArrayList<>();
    final List<envase> envases = new ArrayList<>();
    final List<marcas> marca = new ArrayList<>();
    final List<presentacion> presentaciones = new ArrayList<>();
    public String vtipos = "";
    public String vmedidas = "";
    public String venvase = "";
    public String vmarcas = "";
    public String vestado = "";
    public String viva = "";
    public String vreceta = "";
    public String vpresentacion = "";


    ProgressBar progresoproducto;
    Handler handlerproducto = new Handler();
    boolean isactivoproducto= false;
    private int i = 0;

    public String idproducto = "";

    public String validaregistroactualizado = "";
    //ArrayList<String> tipo;
   // ArrayList<String> medidas;
   // ArrayList<String> envase;
    //ArrayList<String> marcas;

    //para realizar las consultas online
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_producto);

        txtcaja = (TextView) findViewById(R.id.txtmpidcajauser);
        txtidcodigo = (TextView) findViewById(R.id.txtmpidcodigo);
        txtcodigo = (TextView) findViewById(R.id.txtmpcodigo);
        txtnombre =(TextView) findViewById(R.id.txtmpnombre);
        txtdescripcion= (TextView) findViewById(R.id.txtmpdescripcion);



        progresoproducto = (ProgressBar) findViewById(R.id.progressBarproducto);



        txtmppeso = (EditText) findViewById(R.id.txtmppeso);
        txtmpcantidad = (EditText) findViewById(R.id.txtmpcantidad);
        txtmpunidades = (EditText) findViewById(R.id.txtmpunidades);
        txtmpprecio = (EditText) findViewById(R.id.txtmpprecio);


       // tipo=new ArrayList<>();
        spinnertipo = (Spinner) findViewById(R.id.spinnermptipo);

       // medidas=new ArrayList<>();
        spinnermedidas = (Spinner) findViewById(R.id.spinnermpmedidas);
       // envase=new ArrayList<>();
        spinnerenvase = (Spinner) findViewById(R.id.spinnermpenvase);
        //marcas=new ArrayList<>();
        spinnermarcas = (Spinner) findViewById(R.id.spinnermpmarcas);
        spinnerestado = (Spinner) findViewById(R.id.spinnerestadosp);
        spinneriva =  (Spinner) findViewById(R.id.spinneriva);
        spinnerreceta =  (Spinner) findViewById(R.id.spinnerreceta);
        spinnerpresentacion =  (Spinner) findViewById(R.id.spinnerpresentacion);


       btnmgeneraproductototal = (Button)  findViewById(R.id.btnmpguadadatos);
       btnregresaractividadp = (Button)  findViewById(R.id.btnregresarmm);

        consultarconfigp();

       btnmgeneraproductototal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               btnmgeneraproductototal.setEnabled(false);
               inserta_productototal_ws("http://"+ipbdp+":"+puertobdp+"/asofar_app/UpdateProducto/update_producto.php");


               if (!isactivoproducto){

                   Thread hilo = new Thread(new Runnable() {
                       @Override
                       public void run() {
                           while (i<=50){

                               handlerproducto.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       progresoproducto.setProgress(i);

                                   }
                               });

                               try {
                                   Thread.sleep(50);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                               if(i==50){
                                   //  Toast.makeText(getApplicationContext(), "------------------", Toast.LENGTH_LONG).show();
                                   validainserciononlineproducto("http://"+ipbdp+":"+puertobdp+"/asofar_app/Mantenimientos/consulta_productovalida.php?valida=S");

                                   Intent intentT = new Intent(GeneraProducto.this, MainActivity.class);
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
       btnregresaractividadp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), MainActivity.class);

               startActivity(i);
               finish();
           }
       });


/*        String[] frutas ={"mora","guineo","fresa"};  Spinnertipo
        spinnertipo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,frutas));*/

//---------------------------------llamadas.--------------------------------------------
        listartiposp();
        listarmedidassp();
        listarenvasesp();
        listarmarcassp();
        listarpresentacion();


        spinneriva.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viva = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),viva,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerreceta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vreceta = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),vreceta,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerestado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vestado = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),vestado,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String idcaja = getIntent().getStringExtra("id_caja");
        String idcodigo = getIntent().getStringExtra("id_producto");
        String codigoproducto = getIntent().getStringExtra("codigo_producto");
        String nombreproducto = getIntent().getStringExtra("nombre_producto");
        String descripcionproducto = getIntent().getStringExtra("descripcion_codigo");
        validaregistroactualizado = getIntent().getStringExtra("registro_actualizado");

        metodovalidaregistroactualizado();

        txtcaja.setText(""+idcaja);
        txtidcodigo.setText(""+idcodigo);
        txtcodigo.setText(""+codigoproducto);
        txtnombre.setText(""+nombreproducto);
        txtdescripcion.setText(""+descripcionproducto);









}

    public void listartiposp(){
        //para realizar las consultas online
         requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://"+ipbdp+":"+puertobdp+"/asofar_app/Spinner/listar-tipo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Spinnertipo");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String nombretipo=jsonObject1.getString("nombre");
                                String idtipo=jsonObject1.getString("id_tipo");
                                tipos.add(new tipo(idtipo, nombretipo));
                                //frutas.add(id);
                            }
                            //spinnertipo.setAdapter(new ArrayAdapter<String>(GeneraProducto.this, android.R.layout.simple_spinner_dropdown_item,tipo));
                            ArrayAdapter<tipo> arrayAdapter = new ArrayAdapter<>(GeneraProducto.this, android.R.layout.simple_dropdown_item_1line,tipos);
                            spinnertipo.setAdapter(arrayAdapter);
                            spinnertipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    vtipos= tipos.get(i).getIdtipo();
                                    Toast.makeText(getApplicationContext(),vtipos,Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void listarmedidassp(){
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://"+ipbdp+":"+puertobdp+"/asofar_app/Spinner/listar-medidas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Spinnermedidas");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String nombremedida=jsonObject1.getString("nombre_medida");
                                String idmedidas=jsonObject1.getString("id_medidas");
                               // String concat = id +" - "+country;
                                medida.add(new medidas(idmedidas, nombremedida));

                            }
                            //spinnermedidas.setAdapter(new ArrayAdapter<String>(GeneraProducto.this, android.R.layout.simple_spinner_dropdown_item,medidas));
                            ArrayAdapter<medidas> arrayAdapter = new ArrayAdapter<>(GeneraProducto.this, android.R.layout.simple_dropdown_item_1line,medida);
                            spinnermedidas.setAdapter(arrayAdapter);
                            spinnermedidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    vmedidas= medida.get(i).getIdmedidas();
                                    Toast.makeText(getApplicationContext(),vmedidas,Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    public void listarenvasesp(){
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://"+ipbdp+":"+puertobdp+"/asofar_app/Spinner/listar-categoria.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Spinnercategoria");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String nombreenvase=jsonObject1.getString("nombre");
                                String idenvase=jsonObject1.getString("id_categoria");
                                //String concat = id +" - "+country;
                                //envase.add(concat);
                                //frutas.add(id);
                                envases.add(new envase(idenvase, nombreenvase));
                            }
                            //spinnerenvase.setAdapter(new ArrayAdapter<String>(GeneraProducto.this, android.R.layout.simple_spinner_dropdown_item,envase));
                            ArrayAdapter<envase> arrayAdapter = new ArrayAdapter<>(GeneraProducto.this, android.R.layout.simple_dropdown_item_1line,envases);
                            spinnerenvase.setAdapter(arrayAdapter);
                            spinnerenvase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    venvase= envases.get(i).getIdenvase();
                                    Toast.makeText(getApplicationContext(),venvase,Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void listarmarcassp(){
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://"+ipbdp+":"+puertobdp+"/asofar_app/Spinner/listar-marcas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Spinnermarcas");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String nombremarca=jsonObject1.getString("nombre");
                                String idmaras=jsonObject1.getString("id_marcas");
                                //String concat = id +" - "+country;
                                //marcas.add(concat);
                                //frutas.add(id);
                                marca.add(new marcas(idmaras, nombremarca));
                            }
                          //  spinnermarcas.setAdapter(new ArrayAdapter<String>(GeneraProducto.this, android.R.layout.simple_spinner_dropdown_item,marcas));
                            ArrayAdapter<marcas> arrayAdapter = new ArrayAdapter<>(GeneraProducto.this, android.R.layout.simple_dropdown_item_1line,marca);
                            spinnermarcas.setAdapter(arrayAdapter);
                            spinnermarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    vmarcas= marca.get(i).getIdmarcas();
                                    Toast.makeText(getApplicationContext(),vmarcas,Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    public void inserta_productototal_ws(String RUTA) {

        if (txtmppeso.getText().toString().isEmpty() || txtmpcantidad.getText().toString().isEmpty() || txtmpunidades.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "COMPLETA TODOS ", Toast.LENGTH_SHORT).show();

        } else {

            // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RUTA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), "VERIFICANDO......", Toast.LENGTH_SHORT).show();

                        btnmgeneraproductototal.setEnabled(false);

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

                    parametros.put("peso_app", txtmppeso.getText().toString());
                    parametros.put("idtipo_app", vtipos);
                    parametros.put("idmedidas_app", vmedidas);
                    parametros.put("idcategoria_app", venvase);
                    parametros.put("idmarcas_app", vmarcas);
                    parametros.put("estado_app", vestado);
                    parametros.put("iva_app", viva);
                    parametros.put("cantidad_app", txtmpcantidad.getText().toString());
                    parametros.put("receta_app", vreceta);
                    parametros.put("unidades_app", txtmpunidades.getText().toString());
                    parametros.put("estadoactualizado_app", "S");
                    parametros.put("idproductos_app", txtidcodigo.getText().toString());
                    parametros.put("precio_app", txtmpprecio.getText().toString());
                    parametros.put("presentacion_app", vpresentacion);



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

    public void metodovalidaregistroactualizado(){

        if(validaregistroactualizado.equals("S") ){

            btnmgeneraproductototal.setEnabled(false);
            //Toast.makeText(getApplicationContext(), "NO PUEDES ACTUALIZAR ESTE PRODUCTO, REALIZA ESTE PROCESO EN LA APLICACION DE ESCRITORIO",Toast.LENGTH_LONG).show();

          final CharSequence[] opcion ={"ok"};
          final AlertDialog.Builder alerta = new AlertDialog.Builder(GeneraProducto.this);
          alerta.setTitle("NO PUEDES ACTUALIZAR ESTE PRODUCTO");

          alerta.show();

        }else {

            Toast.makeText(getApplicationContext(), ""+validaregistroactualizado,Toast.LENGTH_LONG).show();
        }

    }

    public void listarpresentacion(){
        //para realizar las consultas online
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://"+ipbdp+":"+puertobdp+"/asofar_app/Spinner/listar-presentaciones.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Spinnerpresentacion");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String nombrepresentacion=jsonObject1.getString("nombre");
                                String idpresentacion=jsonObject1.getString("idPresentaciones");
                                presentaciones.add(new presentacion(idpresentacion, nombrepresentacion));
                                //frutas.add(id);
                            }
                            //spinnertipo.setAdapter(new ArrayAdapter<String>(GeneraProducto.this, android.R.layout.simple_spinner_dropdown_item,tipo));
                            ArrayAdapter<presentacion> arrayAdapter = new ArrayAdapter<>(GeneraProducto.this, android.R.layout.simple_dropdown_item_1line,presentaciones);
                            spinnerpresentacion.setAdapter(arrayAdapter);
                            spinnerpresentacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    vpresentacion= presentaciones.get(i).getIdpresentacion();
                                    Toast.makeText(getApplicationContext(),vpresentacion,Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    public void validainserciononlineproducto(String RUTA){




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

                        idproducto = (jsonObject.getString("id_productos"));



                        Toast.makeText(getApplicationContext(), "SE ACTUALIZO DATOS DEL PRODUCTO!!: ", Toast.LENGTH_SHORT).show();
                        btnmgeneraproductototal.setEnabled(false);

                        isactivoproducto = false;


                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                    }
                }
            }

            // Generar un metodo que alerte el error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                Toast.makeText(getApplicationContext(), "ACTUALIZACION NO REGISTRADA,REVISE SU CONEXION WS [ZONA]", Toast.LENGTH_SHORT).show();//error.getMessage()
                btnmgeneraproductototal.setEnabled(true);

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

    public void consultarconfigp(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();


            cajabdp = cursor.getString(1);
            ipbdp= cursor.getString(2);
            puertobdp = cursor.getString(3);

            //  Toast.makeText(getApplicationContext(),""+idprueba+"-"+ipwsprueba+"-"+puertoprueba,Toast.LENGTH_LONG).show();

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }
}
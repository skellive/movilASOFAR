package com.example.asofar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    SQLITE conexion  = new SQLITE(this,"bd_configuracion",null,1);
    //View Objects
    Button buttonScan,btngeneraproducto,btngeneraproductosincodigo, btnconsultacodigos,btnsync, btnconsultacodigows;
    EditText txtcodigo, txtnombrecodigo,txtdescripcionproducto;
    TextView txtidcaja, txtconexiontest,txton,txtoff,tbtnlimpiacampos;
    CheckBox chequeosincodigo;

    public String idproducto = null;
    public String n_caja = null;
    public String estadoactulizacion = null;

    public String cajabd = null;
    public String ipbd = null;
    public String puertobd = null;



    //para realizar las consultas online
            RequestQueue requestQueue;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @SuppressLint("WrongViewCast")
    public   TextView txtmac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //vista de objetos
        buttonScan = (Button) findViewById(R.id.buttonScan);
        btngeneraproducto =(Button) findViewById(R.id.btngeneraproducto);
       // btnconsultacodigos = (Button) findViewById(R.id.btnmoduloconsulta);
        btnconsultacodigows = (Button) findViewById(R.id.btnverificaexistenciacodigows);
        btngeneraproductosincodigo = (Button) findViewById(R.id.btngeneraproductosincodigo);
        btnsync = (Button) findViewById(R.id.btnsync);


        txtcodigo = (EditText) findViewById(R.id.txtcodigo);
        txtidcaja = (TextView) findViewById(R.id.txtcaja);
        txtconexiontest = (TextView) findViewById(R.id.txtconexiontest);
        txtnombrecodigo = (EditText) findViewById(R.id.txtnombrecodgigoproducto);
        txton= (TextView) findViewById(R.id.txton);
        txtoff= (TextView) findViewById(R.id.txtoff);
        tbtnlimpiacampos= (TextView) findViewById(R.id.tbtnlimpiacampos);
        txtdescripcionproducto = (EditText) findViewById(R.id.txtdescripcionproducto);


        chequeosincodigo = (CheckBox) findViewById(R.id.chequeosincodigo);

        consultarconfigmain();


//-------------------------


        txtmac =  (TextView) findViewById(R.id.txtmac);

        //pasar el parametro de la mac(caja)

       n_caja = cajabd;
        txtidcaja.setText(n_caja);

        String mac = getMacAddress();

        if(mac!=""){

            txtmac.setText(mac);

        }

chequeosincodigo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        validasincodigo();
    }
});

        //validacaja();

//intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);


        //Boton Verifica si existe el codigo en ws

        btnconsultacodigows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultacodigoexistencia("http://"+ipbd+":"+puertobd+"/asofar_app/consulta_codigo.php?codigo="+txtcodigo.getText()+"");
            }
        });

        //Boton que genera el ingreso de codigo de un nuevo producto
        btngeneraproducto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insetar_codigo_productows("http://"+ipbd+":"+puertobd+"/asofar_app/inserta_codigo.php");
            }
        });

    btngeneraproductosincodigo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            insetar_sincodigo_productows("http://"+ipbd+":"+puertobd+"/asofar_app/inserta_codigo.php");
        }
    });

        // metodo que valida caja

        verificaexistenciacaja("http://"+ipbd+":"+puertobd+"/asofar_app/consulta_caja.php?idmac="+txtidcaja.getText()+"");
            //Boton que nos direcciona al modulo de consulta
       /* btnconsultacodigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta_codigos();
            }
        });*/

        btnsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enviaparametrosconsultacodigos();
            }
        });

        tbtnlimpiacampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtcodigo.setText("");
                txtnombrecodigo.setText("");
                txtdescripcionproducto.setText("");
                txtcodigo.setEnabled(false);
                txtnombrecodigo.setEnabled(false);
                txtdescripcionproducto.setEnabled(false);
                btngeneraproducto.setEnabled(false);
                btngeneraproductosincodigo.setEnabled(false);
                btnsync.setEnabled(false);
            }
        });

    }

    //Obtener los resultados del escaneo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                txtcodigo.setText("");
                Toast.makeText(this, "No hay Codigo Scanneado!!", Toast.LENGTH_LONG).show();
            } else {
                txtcodigo.setText("");
                   // Toast.makeText(this, "Hola"+ result.getContents(), Toast.LENGTH_LONG).show();
                        txtcodigo.setText(result.getContents());





            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
        btnsync.setEnabled(false);
    }
// Metodo inserta codigo de producto

    public void consultacodigoexistencia(String RUTA){

            if (txtcodigo.getText().toString().isEmpty()) {

                Toast.makeText(this, "Campo Vacio, Por Favor Scannea un Codigo para realizar la verificacion", Toast.LENGTH_LONG).show();
            } else {
                String codigo = null;

                codigo = txtcodigo.getText().toString();


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
                                txtnombrecodigo.setText(jsonObject.getString("nombre"));
                                txtdescripcionproducto.setText(jsonObject.getString("descripcion"));
                                idproducto = (jsonObject.getString("id_productos"));
                                estadoactulizacion = (jsonObject.getString("registro_actualizado"));
                                btnsync.setEnabled(true);

                                Toast.makeText(getApplicationContext(), "CODIGO EXISTE: "+idproducto, Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {

                                Toast.makeText(getApplicationContext(), "ZONA VALIDA CODIGO: " + e.getMessage(), Toast.LENGTH_SHORT).show();//

                            }
                        }
                    }

                    // Generar un metodo que alerte el error
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "CODIGO NO REGISTRADO [ZONA VALIDA CODIGO]", Toast.LENGTH_SHORT).show();//error.getMessage()

                        if (chequeosincodigo.isChecked() == true) {
                            btngeneraproductosincodigo.setEnabled(true);
                            txtnombrecodigo.setEnabled(true);
                            txtnombrecodigo.setText("");
                            txtdescripcionproducto.setEnabled(true);
                            txtdescripcionproducto.setText("");

                        } else {
                            btngeneraproductosincodigo.setEnabled(false);
                            btngeneraproducto.setEnabled(true);
                            txtnombrecodigo.setEnabled(true);
                            txtdescripcionproducto.setEnabled(true);
                            txtdescripcionproducto.setText("");
                            txtnombrecodigo.setText("");
                        }
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


      //inserta codigo en ws

    public void insetar_codigo_productows(String RUTA){

        if (txtcodigo.getText().toString().isEmpty() || txtnombrecodigo.getText().toString().isEmpty() || txtdescripcionproducto.getText().toString().isEmpty() ) {

            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
        } else {

            // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RUTA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                    btngeneraproducto.setEnabled(false);
                    txtcodigo.setEnabled(false);
                    txtnombrecodigo.setEnabled(false);
                    txtdescripcionproducto.setEnabled(false);
                    txtcodigo.setText("");
                    txtnombrecodigo.setText("");
                    txtdescripcionproducto.setText("");
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

                    parametros.put("nombrecodigobr_app", txtnombrecodigo.getText().toString());
                    parametros.put("descripcioncodigo_app", txtdescripcionproducto.getText().toString());
                    parametros.put("idcajargistracodigo_app", txtidcaja.getText().toString());
                    parametros.put("codigobarras_app", txtcodigo.getText().toString());


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
        // metodo que te envia al modulo de consulta de codigos
        /*public void consulta_codigos(){

        Intent moduloconsulta = new Intent(this,Buscar_Codigo.class);
        startActivity(moduloconsulta);

         <Button
        android:id="@+id/btnmoduloconsulta"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:drawableBottom="@android:drawable/ic_menu_agenda"
        android:text="CODIGO REGISTRADOS"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.859" />


    };*/

        //Validar la mac del equipo como identificador

        public  String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.d("Error", ex.getMessage());
        }
        return "";
    }

        //busqueda por base, si existe o no el registro del codigo
      /*  public void validacaja(){

        // Llamar a la conexion
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "BD_ASOFAR_APP", null, 1);

        // Generamos Objeto
        SQLiteDatabase db = conn.getWritableDatabase();

            //parametro a realizar la consulta;

       String[] parametro = {"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"};
          // String[] parametro = {txtmac.getText().toString()};
            String[] campos = {"MAC_EQUIPO"};

            try {


                // Realizamos la consulta
                Cursor cursor = db.query("CAJA", campos, "MAC_EQUIPO=?", parametro, null, null, null);
                // que me separe los datos que lanze en mi consulta
                cursor.moveToFirst();

                // asignacion de datos de la consulta por oreden desde 0 como primer parametro hasta N
                //txtmuestraidconsulta.setText(cursor.getString(0));

                cursor.close();
                Toast.makeText(getApplicationContext(), "EQUIPO REE", Toast.LENGTH_SHORT).show();



            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "EQUIPO NO REGISTRADO", Toast.LENGTH_SHORT).show();
               // finish();

            };

        }*/

    /* public void irmodulosync(){

        Intent modulosync = new Intent(this,GeneraProducto.class);
        startActivity(modulosync);




    }*/


    //--------------------------------------------------Valida caja-------------------------------------------------------------


    private void verificaexistenciacaja(String RUTA){
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
                        //txtnombre.setText(jsonObject.getString("nombre_prueba_app"));
                        Toast.makeText(getApplicationContext(), "CAJA AUTORIZADA", Toast.LENGTH_SHORT).show();
                        txton.setVisibility(View.VISIBLE);
                        txtoff.setVisibility(View.GONE);
                        String dbvalida = "Online";

                        txtconexiontest.setText(dbvalida);



                    }catch (JSONException e) {

                        Toast.makeText(getApplicationContext(),"ZONA VALIDA CAJA: "+e.getMessage() , Toast.LENGTH_SHORT).show();//
                    }
                }
            }

            // Generar un metodo que alerte el error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE COMUNICACION WS O CAJA NO AUTORIZADA", Toast.LENGTH_SHORT).show();//error.getMessage()
                txton.setVisibility(View.GONE);
                txtoff.setVisibility(View.VISIBLE);
                String dbvalida = "Offline";

                txtconexiontest.setText(dbvalida);
            }
        });

        // ahora comiamos el servicio y lo pegamos donde inicializamos los onjetos osea arriba --  RequestQueue requestQueue

        //
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void validasincodigo(){

        if(chequeosincodigo.isChecked()== true){
            buttonScan.setEnabled(false);
            btngeneraproducto.setEnabled(false);
            txtcodigo.setEnabled(true);
            txtcodigo.setText("");
            txtnombrecodigo.setText("");
            txtnombrecodigo.setEnabled(false);
            txtdescripcionproducto.setEnabled(false);
            txtdescripcionproducto.setText("");
        }else{

            buttonScan.setEnabled(true);
            btngeneraproductosincodigo.setEnabled(false);
            btngeneraproducto.setEnabled(false);
            txtcodigo.setEnabled(false);
            txtnombrecodigo.setEnabled(false);
            txtcodigo.setText("");
            txtnombrecodigo.setText("");

        }
    }


    public void insetar_sincodigo_productows(String RUTA){


        if (txtcodigo.getText().toString().isEmpty() || txtnombrecodigo.getText().toString().isEmpty()|| txtdescripcionproducto.getText().toString().isEmpty()) {

            Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show();
        } else {

            // 1- definir el metodo de comunicaion de nuestro web services, cuando lleguemos a renponse se generara el metodo listener

            StringRequest stringRequest = new StringRequest(Request.Method.POST, RUTA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                    btngeneraproductosincodigo.setEnabled(false);
                    txtcodigo.setEnabled(false);
                    txtnombrecodigo.setEnabled(false);
                    txtdescripcionproducto.setEnabled(false);
                    txtcodigo.setText("");
                    txtnombrecodigo.setText("");
                    txtdescripcionproducto.setText("");

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

                    parametros.put("nombrecodigobr_app", txtnombrecodigo.getText().toString());
                    parametros.put("descripcioncodigo_app", txtdescripcionproducto.getText().toString());
                    parametros.put("idcajargistracodigo_app", txtidcaja.getText().toString());
                    parametros.put("codigobarras_app", txtcodigo.getText().toString());

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

    public void Enviaparametrosconsultacodigos(){

         Intent i = new Intent(this, GeneraProducto.class);
         i.putExtra("id_producto",idproducto);
        i.putExtra("codigo_producto",txtcodigo.getText().toString());
        i.putExtra("nombre_producto",txtnombrecodigo.getText().toString());
        i.putExtra("descripcion_codigo",txtdescripcionproducto.getText().toString());
        i.putExtra("id_caja",n_caja);
        i.putExtra("registro_actualizado",estadoactulizacion);

        startActivity(i);
        finish();

    }

    public void consultarconfigmain(){

        SQLiteDatabase db = conexion.getReadableDatabase();

        String codbusca = "1";
        String[] parametro = {codbusca};
        String[] campos = {"iddb","cajadb","ipserverdb","puertodb"};// TEXT,  TEXT,  TEXT

        try{
            Cursor cursor = db.query("config", campos, "iddb=?",parametro,null, null, null);
            cursor.moveToFirst();


            cajabd = cursor.getString(1);
            ipbd= cursor.getString(2);
            puertobd = cursor.getString(3);

          //  Toast.makeText(getApplicationContext(),""+idprueba+"-"+ipwsprueba+"-"+puertoprueba,Toast.LENGTH_LONG).show();

            cursor.close();


        }catch (Exception e){

            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
        }



    }

    }



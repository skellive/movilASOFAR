package com.java.movilasofar.vista;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.java.movilasofar.modelo.Proveedor;
import com.loopj.android.http.*;
import com.java.movilasofar.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class crear_producto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Spinner spinnerProveedor;
    public Context context;
    public AsyncHttpClient cliente;
    List<String> listaProveedor_id;
    private ArrayList<String> listarProveedor_nombre;
    public crear_producto() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static crear_producto newInstance(String param1, String param2) {
        crear_producto fragment = new crear_producto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_crear_producto, container, false);
        context = root.getContext();
       spinnerProveedor =  root.findViewById(R.id.txtProveedor);
       List<String> listaProveedor_id = null;
       List<String> listarProveedor_nombre = null;
      cliente = new AsyncHttpClient();
        llenarProveedor();

        return root;
    }

    private void llenarProveedor(){


       RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.5:8000/Asofarphp/producto/consultas/Proveedor.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        cargarProveedor(jsonArray);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        );

        queue.add(stringRequest);

    }


    private void cargarProveedor(JSONArray jsonArray){


       listaProveedor_id = new ArrayList<String>();
        listarProveedor_nombre = new ArrayList<String>();
        for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id_proveedor = jsonObject.getString("id_proveedor");
                String nombre = jsonObject.getString("nombre");
                listaProveedor_id.add(id_proveedor);
                listarProveedor_nombre.add(nombre);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapterProveedor = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, listarProveedor_nombre);
        spinnerProveedor.setAdapter(adapterProveedor);


    }
}

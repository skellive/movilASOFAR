package com.java.movilasofar.vista;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.java.movilasofar.modelo.Proveedor;
import com.loopj.android.http.*;

import com.java.movilasofar.R;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class crear_producto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Spinner spinnerProveedor;
    public Context context;
    private AsyncHttpClient cliente;
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
        llenarProveedor();
        cliente = new AsyncHttpClient();

        return root;
    }

    private void llenarProveedor(){
        String url = "http://192.168.1.5:8000/Asofarphp/producto/consultas/Proveedor.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    cargarProveedor(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }


    private void cargarProveedor(String respuesta){
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);

            for (int i =0; i< jsonArreglo.length(); i++){
                Proveedor p = new Proveedor();
                p.setNombre(jsonArreglo.getJSONObject(i).getString("nombre"));

                lista.add(p);
            }
            ArrayAdapter<Proveedor> a = new ArrayAdapter<Proveedor>(getActivity(), android.R.layout.simple_dropdown_item_1line, lista);

            spinnerProveedor.setAdapter(a);
        }catch (Exception e){
            e.printStackTrace();

        }


    }
}

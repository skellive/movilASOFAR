package com.java.movilasofar.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.java.movilasofar.R;

public class list_productos_seleccionar extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_productos_seleccionar);
        Intent intent = getIntent();
        intent.getAction();
    }


    private void obtenerProductos(){
        String url = "";
    }

}

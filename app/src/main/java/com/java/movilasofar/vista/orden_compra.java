package com.java.movilasofar.vista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.java.movilasofar.R;


public class orden_compra extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button btnAgreProducts;
    public Context context;

    public orden_compra() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static orden_compra newInstance(String param1, String param2) {
        orden_compra fragment = new orden_compra();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root =  inflater.inflate(R.layout.fragment_orden_compra, container, false);
        btnAgreProducts = root.findViewById(R.id.btnAgreProducts);
        btnAgreProducts.setOnClickListener(mOnClickListener_agregar);
        context = root.getContext();
        return root;
    }

    public View.OnClickListener mOnClickListener_agregar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(orden_compra.this.getContext(),
                    list_productos_seleccionar.class);
            startActivity(intent);
        }
    };
}
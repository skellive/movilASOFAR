package com.java.movilasofar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link barcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class barcode extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button btnScanner;
    public TextView tvBarCode;
    public Context c;



    public static barcode newInstance(String param1, String param2) {
        barcode fragment = new barcode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View root = inflater.inflate(R.layout.fragment_barcode, container, false);
        c = root.getContext();
        btnScanner = root.findViewById(R.id.btnScanner);
        tvBarCode = root.findViewById(R.id.tvBarCode);

        btnScanner.setOnClickListener(mOnClickListener);

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result != null)
                if (result.getContents() != null){
                    tvBarCode.setText("Su codigo de barra es: " + result.getContents());
                }else{
                    tvBarCode.setText("Error al escanear el codigo de barras");
                }
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btnScanner) {
                new IntentIntegrator(getActivity()).initiateScan();
            }
        }
    };

}
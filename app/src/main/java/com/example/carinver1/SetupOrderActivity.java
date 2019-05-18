package com.example.carinver1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SetupOrderActivity extends AppCompatActivity {

    TextView sPemilik,sHarga,sLokasi,sNoTelephone;
    Toolbar sToolbar;
    private String pemilik,harga,lokasi,notelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_order);
        initialize();
    }

    private void initialize() {
        setToolbar();
        sHarga = (TextView) findViewById(R.id.setupOrderHarga);
        sLokasi = (TextView) findViewById(R.id.setupOrderLokasi);
        sNoTelephone = (TextView) findViewById(R.id.setupOrderNoTelp);
        sPemilik = (TextView) findViewById(R.id.setupOrderPemilik);

        harga =  getIntent().getExtras().get("harga").toString();
        pemilik =  getIntent().getExtras().get("pemilik").toString();
        lokasi =  getIntent().getExtras().get("alamat").toString();
        notelp =  getIntent().getExtras().get("notelephone").toString();

        sHarga.setText("Harga : "+harga+ " /Hari");
        sLokasi.setText("Lokasi : "+lokasi);
        sNoTelephone.setText("No Telephone : "+notelp);
        sPemilik.setText("Pemilik : "+pemilik);
    }

    private void setToolbar() {

        sToolbar = (Toolbar) findViewById(R.id.setup_order_activity);
        setSupportActionBar(sToolbar);
        getSupportActionBar().setTitle("Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

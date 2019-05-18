package com.example.carinver1;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SetupOrderActivity extends AppCompatActivity {

    TextView sPemilik, sHarga, sLokasi, sNoTelephone, sJudul;
    Toolbar sToolbar;
    Button etglMasuk, etglKeluar,btnBooking;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateMasuk,dateKeluar;
    private String pemilik, harga, lokasi, notelp, judul,currentUserID,saveCurrentDate,saveCurrentTime,orderRandomID,gambar,deskripsi;
    FirebaseAuth mAuth;
    DatabaseReference orderRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_order);
        initialize();
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderanUser();
            }
        });
    }

    private void saveOrderanUser()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar1.getTime());

        orderRandomID = saveCurrentDate + saveCurrentTime;

        if(TextUtils.isEmpty(etglKeluar.getText().toString()))
        {
            Toast.makeText(this, "Masukkan tgl kembali", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(etglMasuk.getText().toString()))
        {
            Toast.makeText(this, "Masukkan Tgl Pinjam", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap orderMap = new HashMap();
            orderMap.put("gambarorder",gambar);
            orderMap.put("deskripsiorder",deskripsi);
            orderMap.put("pemilikorder",pemilik);
            orderMap.put("hargaorder",harga);
            orderMap.put("judulorder",judul);
            orderMap.put("time",saveCurrentTime);
            orderMap.put("date",saveCurrentDate);
            orderMap.put("tglpinjam",etglMasuk.getText().toString());
            orderMap.put("tglkembali",etglKeluar.getText().toString());
            orderMap.put("notelephoneorder",notelp);
            orderMap.put("alamatorder",lokasi);

            orderRefs.child(currentUserID + orderRandomID).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SetupOrderActivity.this, "Berhasil Booking", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SetupOrderActivity.this, "Gagal Booking : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void initialize()
    {
        setToolbar();
        sHarga = (TextView) findViewById(R.id.setupOrderHarga);
        sLokasi = (TextView) findViewById(R.id.setupOrderLokasi);
        sNoTelephone = (TextView) findViewById(R.id.setupOrderNoTelp);
        sPemilik = (TextView) findViewById(R.id.setupOrderPemilik);
        sJudul = (TextView) findViewById(R.id.setupOrderJudul);
        btnBooking = (Button) findViewById(R.id.btnBooking);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        orderRefs = FirebaseDatabase.getInstance().getReference().child("Orders");

        etglKeluar = (Button) findViewById(R.id.setupOrderTglKembali);
        etglMasuk = (Button) findViewById(R.id.setupOrderTglPinjam);
        dateMasuk = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateKeluar = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        etglMasuk.setEnabled(true);
        etglKeluar.setEnabled(true);

        etglKeluar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showDateDialogKeluar();
            }
        });
        etglMasuk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showDateDialogMasuk();
            }
        });

        harga = getIntent().getExtras().get("harga").toString();
        pemilik = getIntent().getExtras().get("pemilik").toString();
        lokasi = getIntent().getExtras().get("alamat").toString();
        notelp = getIntent().getExtras().get("notelephone").toString();
        judul = getIntent().getExtras().get("judul").toString();
        deskripsi = getIntent().getExtras().get("deskripsi").toString();
        gambar = getIntent().getExtras().get("gambar").toString();

        sHarga.setText("Harga : " + harga + " /Hari");
        sLokasi.setText("Lokasi : " + lokasi);
        sNoTelephone.setText("No Telephone : " + notelp);
        sPemilik.setText("Pemilik : " + pemilik);
        sJudul.setText(judul);

    }

    private void showDateDialogMasuk()
    {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etglMasuk.setText(dateMasuk.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDateDialogKeluar()
    {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etglKeluar.setText(dateKeluar.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void setToolbar()
    {

        sToolbar = (Toolbar) findViewById(R.id.setup_order_activity);
        setSupportActionBar(sToolbar);
        getSupportActionBar().setTitle("Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

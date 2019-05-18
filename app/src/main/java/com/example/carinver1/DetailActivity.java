package com.example.carinver1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView tvPemilik,tvAlamat,tvNoTelp,tvDesktipsi,tvJudul;
    ImageView ivGambar;
    private String pemilik,alamat,notelp,deskripsi,judul,gambar;
    Button btnChat,btnOrder;
    Toolbar detailToolbar;
    DatabaseReference postRefs,orderRefs;
    FirebaseAuth mAuth;
    private String currentUserID,postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initialize();
        postRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    pemilik = dataSnapshot.child("namalengkap").getValue().toString();
                    alamat = dataSnapshot.child("alamat").getValue().toString();
                    notelp = dataSnapshot.child("phonenumber").getValue().toString();
                    deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                    judul = dataSnapshot.child("judul").getValue().toString();
                    gambar = dataSnapshot.child("postimage").getValue().toString();

                    tvAlamat.setText("Lokasi : "+alamat);
                    tvDesktipsi.setText(deskripsi);
                    tvJudul.setText(judul);
                    tvNoTelp.setText("No.Telp : "+notelp);
                    tvPemilik.setText("Pemilik : "+pemilik);

                    Picasso.with(DetailActivity.this).load(gambar).into(ivGambar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderanUser();
            }
        });

    }
    public void saveOrderanUser()
    {

    }
    public void initialize()
    {
        setToolbar();
        tvAlamat = (TextView) findViewById(R.id.detailLokasi);
        tvPemilik = (TextView) findViewById(R.id.detailPemilik);
        tvNoTelp = (TextView) findViewById(R.id.detailNoTelp);
        tvDesktipsi = (TextView) findViewById(R.id.detailDeskripsi);
        tvJudul = (TextView) findViewById(R.id.detailJudul);
        ivGambar = (ImageView) findViewById(R.id.detailImage);
        btnChat = (Button) findViewById(R.id.btnChat);
        btnOrder = (Button) findViewById(R.id.btnOrder);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("Postkey").toString();
        postRefs = FirebaseDatabase.getInstance().getReference().child("post").child(postKey);
        orderRefs = FirebaseDatabase.getInstance().getReference().child("order");

    }

    private void setToolbar() {
        detailToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}

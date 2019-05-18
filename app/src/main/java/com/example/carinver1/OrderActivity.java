package com.example.carinver1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView orderList1;
    DatabaseReference orderRefs;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initilize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayAllOrder();
    }

    private void initilize() {

        orderList1 = (RecyclerView) findViewById(R.id.orderList);
        orderList1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        orderList1.setLayoutManager(linearLayoutManager);
        orderRefs = FirebaseDatabase.getInstance().getReference().child("Orders");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.orderNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.itemOrder);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.itemHome:
                        Intent homeIntent = new Intent(OrderActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.itemOrder:
                        break;
                    case R.id.itemProfile:
                        Intent profIntent = new Intent(OrderActivity.this,ProfileActivity.class);
                        startActivity(profIntent);
                        finish();
                        break;
                    case R.id.itemUpload:
                        Intent postIntent = new Intent(OrderActivity.this,PostActivity.class);
                        startActivity(postIntent);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    private void displayAllOrder()
    {

        FirebaseRecyclerOptions<Order> options=
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(orderRefs,Order.class)
                        .setLifecycleOwner(this)
                        .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {


                        final String PostKey = getRef(position).getKey();


                        holder.setAlamatOrder(model.getAlamatorder());
                        holder.setDeskripsiOrder(model.getDeskripsiorder());
                        holder.setHarga(model.getHargaorder());
                        holder.setJudulOrder(model.getJudulorder());
                        holder.setNoTelpOrder(model.getNotelephoneorder());
                        holder.setOrderImage(getApplicationContext(),model.getGambarorder());
                        holder.setPemilikOrder(model.getPemilikorder());
                        holder.setTglKembaliOrder(model.getTglkembali());
                        holder.setTglPinjamOrder(model.getTglpinjam());

                        holder.purchase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        holder.delete.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                                orderRefs.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(OrderActivity.this, "Berhasil Hapus Booking", Toast.LENGTH_SHORT).show();
                                    }
                                });
                           }
                        });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        return new OrderViewHolder(LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.all_order_layout, viewGroup, false));
                    }
                };
        orderList1.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder
    {
        Button purchase,delete;
        View mView;
        public OrderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            purchase = (Button) mView.findViewById(R.id.orderPurchase);
            delete = (Button) mView.findViewById(R.id.orderDelete);
        }

        public void setJudulOrder(String judulP) {
            TextView judul = (TextView) mView.findViewById(R.id.orderJudul);
            judul.setText(judulP);
        }

        public void setDeskripsiOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderDeskripsi);
            Deskripsi.setText(desk);
        }

        public void setHarga(String harga) {
            TextView hargasPos = (TextView) mView.findViewById(R.id.orderHarga);
            hargasPos.setText("Harga : "+harga + " /hari");
        }

        public void setOrderImage(Context ctx, String profileImage) {
            ImageView image = (ImageView) mView.findViewById(R.id.orderImage);
            Picasso.with(ctx).load(profileImage).into(image);
        }
        public void setTglKembaliOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderTglKembali);
            Deskripsi.setText("Tgl Kembali : "+desk);
        }
        public void setTglPinjamOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderTglPinjam);
            Deskripsi.setText("Tgl Pinjam: "+desk);
        }
        public void setPemilikOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderPemilik);
            Deskripsi.setText("Pemilik: "+desk);
        }
        public void setNoTelpOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderNoTelp);
            Deskripsi.setText("No.Telp: "+desk);
        }
        public void setAlamatOrder(String desk) {
            TextView Deskripsi = (TextView) mView.findViewById(R.id.orderAlamat);
            Deskripsi.setText("Lokasi: "+desk);
        }

    }
}

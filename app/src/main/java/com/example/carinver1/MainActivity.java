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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

 public class MainActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
     BottomNavigationView bottomNavigationView;
     RecyclerView postView;
     DatabaseReference postRef;
     private FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter;
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postRef = FirebaseDatabase.getInstance().getReference().child("post");
        postView = (RecyclerView) findViewById(R.id.list_all_vehicle);
        postView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        postView.setLayoutManager(linearLayoutManager);
        initialize();
    }

     private void initialize()
     {
         mAuth = FirebaseAuth.getInstance();
         bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainNavigationBar);
         bottomNavigationView.setSelectedItemId(R.id.itemHome);
         bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {
                 switch (menuItem.getItemId()){
                     case R.id.itemHome:

                         break;
                     case R.id.itemOrder:
                         Intent orderIntent = new Intent(MainActivity.this,OrderActivity.class);
                         startActivity(orderIntent);
                         finish();
                         break;
                     case R.id.itemProfile:
                         Intent profIntent = new Intent(MainActivity.this,ProfileActivity.class);
                         startActivity(profIntent);
                         finish();
                         break;
                     case R.id.itemUpload:
                         sentToPostActivity();
                         break;
                 }
                 return true;
             }
         });

     }

     private void displayAllPost()
     {

         FirebaseRecyclerOptions<Post> options=
                 new FirebaseRecyclerOptions.Builder<Post>()
                         .setQuery(postRef,Post.class)
                         .setLifecycleOwner(this)
                         .build();

         firebaseRecyclerAdapter =
                 new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
                     @Override
                     protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                         final String PostKey = getRef(position).getKey();

                         holder.setJudulPostingan(model.getJudul());
                         holder.setDeskripsiPostingan(model.getDeskripsi());
                         holder.setHarga(model.getHarga());
                         holder.setPostImage(getApplicationContext(),model.getPostimage());


                         holder.mView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 Intent clickPostIntent = new Intent(MainActivity.this,DetailActivity.class);
                                 clickPostIntent.putExtra("Postkey",PostKey);
                                 startActivity(clickPostIntent);
                             }
                         });

                     }

                     @NonNull
                     @Override
                     public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                         return new PostViewHolder(LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.all_post_layout, viewGroup, false));
                     }
                 };
         postView.setAdapter(firebaseRecyclerAdapter);
    }

     private void sentToPostActivity()
     {
        Intent postIntent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(postIntent);
        finish();
     }

     @Override
     protected void onStart()
     {
         super.onStart();
         displayAllPost();
     }

     public static class PostViewHolder extends RecyclerView.ViewHolder
     {
         View mView;
         public PostViewHolder(View itemView) {
             super(itemView);
             mView = itemView;
         }

         public void setJudulPostingan(String judulP) {
             TextView judul = (TextView) mView.findViewById(R.id.postJudul);
             judul.setText(judulP);
         }

         public void setDeskripsiPostingan(String desk) {
             TextView Deskripsi = (TextView) mView.findViewById(R.id.postDeskripsi);
             Deskripsi.setText(desk);
         }

         public void setHarga(String harga) {
             TextView hargasPos = (TextView) mView.findViewById(R.id.postHarga);
             hargasPos.setText(harga + " /hari");
         }

         public void setPostImage(Context ctx, String profileImage) {
             ImageView image = (ImageView) mView.findViewById(R.id.postImage);
             Picasso.with(ctx).load(profileImage).into(image);
         }
     }

 }

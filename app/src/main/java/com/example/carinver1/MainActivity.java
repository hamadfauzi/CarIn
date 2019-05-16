 package com.example.carinver1;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

     private void initialize()
     {
         mAuth = FirebaseAuth.getInstance();
         bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainNavigationBar);
         bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {
                 switch (menuItem.getItemId()){
                     case R.id.itemHome:

                         break;
                     case R.id.itemOrder:

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

     private void sentToPostActivity() {
        Intent postIntent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(postIntent);
        finish();
     }

     private void sentToLoginActivity()
     {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
     }

 }

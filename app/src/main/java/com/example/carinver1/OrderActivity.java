package com.example.carinver1;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class OrderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initilize();
    }

    private void initilize() {

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
}

package com.monjed.maswadeh;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class Details extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();

        TextView detName = findViewById(R.id.searchName);
        TextView detAddress = findViewById(R.id.detAddress);
        TextView detPhone = findViewById(R.id.detPhone);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String address = bundle.getString("address");
        String phoneNum = bundle.getString("phoneNum");

        detName.setText(name);
        detAddress.setText(address);
        detPhone.setText(phoneNum);
    }
}
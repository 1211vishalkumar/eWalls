package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddingNewProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_adding_new_product);

        Toast.makeText(AdminAddingNewProductActivity.this,"Welcome Admin",Toast.LENGTH_SHORT).show();
    }
}

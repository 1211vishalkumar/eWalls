package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Customer.HomeActivity;
import com.example.myapplication.Customer.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Sellers.SellerProductCatlogActivity;



public class AdminHomeActivity extends AppCompatActivity {

    private Button btnAdminSignOut,btnCheckOrders,btnAdminMaintainProducts,btnAdminApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        btnAdminSignOut = findViewById(R.id.btnAdminSignOut);
        btnCheckOrders = findViewById(R.id.btnCheckOrders);
        btnAdminMaintainProducts = findViewById(R.id.btnAdminMaintainProducts);
        btnAdminApprove = findViewById(R.id.btnAdminApprove);

        btnAdminMaintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        btnAdminSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCheckOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        btnAdminApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,CheckAdminNewProductsActivity.class);
                startActivity(intent);
            }
        });

    }
}

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCatlogActivity extends AppCompatActivity {
    private ImageView ivSketches,ivScenery,ivAbstractArt,ivMadhubaniArt;
    private ImageView ivGlass,ivMicron,ivCubism,ivIllustration;
    private ImageView ivModernArt,ivPopArt,ivGeometric,ivSpiritual;
    private Button btnAdminSignOut,btnCheckOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catlog);


        ivSketches = findViewById(R.id.ivSketches);
        ivScenery=findViewById(R.id.ivScenery);
        ivAbstractArt =findViewById(R.id.ivAbstractArt);
        ivMadhubaniArt=findViewById(R.id.ivMadhubaniArt);

        ivGlass=findViewById(R.id.ivGlass);
        ivMicron=findViewById(R.id.ivMicron);
        ivCubism=findViewById(R.id.ivCubism);
        ivIllustration =findViewById(R.id.ivIllustration);

        ivModernArt=findViewById(R.id.ivModernArt);
        ivPopArt = findViewById(R.id.ivPopArt);
        ivGeometric= findViewById(R.id.ivGeometric);
        ivSpiritual = findViewById(R.id.ivSpiritual);

        btnAdminSignOut = findViewById(R.id.btnAdminSignOut);
        btnCheckOrders = findViewById(R.id.btnCheckOrders);

        btnAdminSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCheckOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });


        ivSketches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","sketches");
                startActivity(intent);
            }
        });

        ivScenery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","scenery");
                startActivity(intent);
            }
        });
        ivAbstractArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","abstractArt");
                startActivity(intent);
            }
        });
        ivMadhubaniArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","madhubaniArt");
                startActivity(intent);
            }
        });
        ivGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","glass");
                startActivity(intent);
            }
        });

        ivMicron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","micron");
                startActivity(intent);
            }
        });
        ivCubism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","cubism");
                startActivity(intent);
            }
        });
        ivIllustration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","illustration");
                startActivity(intent);
            }
        });
        ivModernArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","modernArt");
                startActivity(intent);
            }
        });

        ivPopArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","popArt");
                startActivity(intent);
            }
        });
        ivGeometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","geometric");
                startActivity(intent);
            }
        });
        ivSpiritual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatlogActivity.this,AdminAddingNewProductActivity.class);
                intent.putExtra("category","spiritual");
                startActivity(intent);
            }
        });

    }
}

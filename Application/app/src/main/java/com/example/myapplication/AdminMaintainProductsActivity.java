package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText edProdNameAdminMaintain,edProdDescriptionAdminMaintain,edProdPriceAdminMaintain;
    private Button btnApplyChangeMaintainAdmin,btnDeleteProductMaintainAdmin;
    private ImageView ivProdImageAdminMaintain;
    private String productID = "";
    private DatabaseReference productRef ;
    //private StorageReference productStorageRef;  // we should also remove the product from the storage  write by you self

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        //  productStorageRef = FirebaseStorage.getInstance().getReference().child("Product Images");     //   <-


        edProdDescriptionAdminMaintain = findViewById(R.id.edProdDescriptionAdminMaintain);
        edProdNameAdminMaintain = findViewById(R.id.edProdNameAdminMaintain);
        edProdPriceAdminMaintain = findViewById(R.id.edProdPriceAdminMaintain);

        btnApplyChangeMaintainAdmin = findViewById(R.id.btnApplyChangeMaintainAdmin);
        btnDeleteProductMaintainAdmin = findViewById(R.id.btnDeleteProductMaintainAdmin);

        ivProdImageAdminMaintain = findViewById(R.id.ivProdImageAdminMaintain);

        displaySpecificProductInfo();

        btnApplyChangeMaintainAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a method
                applyTheChanges();
            }
        });

        btnDeleteProductMaintainAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we gonna create method to dwlete the product
                deleteThisProduct();
            }
        });

    }

    private void deleteThisProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminCatlogActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "This product is deleted Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void applyTheChanges() {

        //getting the data from the fields
        String pNameNew =edProdNameAdminMaintain.getText().toString();
        String pPriceNew = edProdPriceAdminMaintain.getText().toString();
        String pDescriptionNew = edProdDescriptionAdminMaintain.getText().toString();
        if(pNameNew.equals("")){

            Toast.makeText(this, "write the  product name", Toast.LENGTH_SHORT).show();
        }else if(pPriceNew.equals("")){

            Toast.makeText(this, "write the  product price", Toast.LENGTH_SHORT).show();
        }else if(pDescriptionNew.equals("")){

            Toast.makeText(this, "write the  product Description", Toast.LENGTH_SHORT).show();
        }
        else{
            // if everything is ok then we gonna do the changes/ store the chnages in the dataBase
            HashMap<String,Object> productMap  = new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",pDescriptionNew);
            productMap.put("price",pPriceNew);
            productMap.put("pname",pNameNew);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //here we gonna notify the Admin
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();
                        // now we have to send the admin to AdminCatlogActivity
                        Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminCatlogActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }



    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    //Dispalying the old data
                    edProdNameAdminMaintain.setText(pName);
                    edProdDescriptionAdminMaintain.setText(pDescription);
                    edProdPriceAdminMaintain.setText(pPrice);
                    Picasso.get().load(pImage).into(ivProdImageAdminMaintain);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

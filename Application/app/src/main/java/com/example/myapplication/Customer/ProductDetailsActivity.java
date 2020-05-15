package com.example.myapplication.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    Button btnAddToCartDetails;
    ImageView ivProductImageDetails;
    ElegantNumberButton numberButton;
    TextView tvProductNameDetails,tvProductDesDetails,tvProductPriceDetails;
    private String productID = "",status = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        btnAddToCartDetails = findViewById(R.id.btnAddToCartDetails);

        ivProductImageDetails = findViewById(R.id.ivProductImageDetails);

        numberButton = findViewById(R.id.numberButton);

        tvProductNameDetails = findViewById(R.id.tvProductNameDetails);
        tvProductDesDetails = findViewById(R.id.tvProductDesDetails);
        tvProductPriceDetails = findViewById(R.id.tvProductPriceDetails);

        // now with the help of productId we goona retribve all the info about that product
        //for this we gonna make a method
        getProductDetails(productID);

        btnAddToCartDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here we gonna create a method which gonna add products to cart list


                if(status.equals("Order Placed") || status.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more products once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }else{
                    addingToCartList();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderStatus();
    }

    private void addingToCartList() {

        String saveCurrentTIme,saveCurrentDate;

        // here we need to get the time and date that which time the user is going to purchase the item
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTIme = currentDate.format(calForDate.getTime());

        // now we gonna store these into firebase Database
        final  DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        //by using the HshMap we are going to store the data
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",tvProductNameDetails.getText().toString());
        cartMap.put("price",tvProductPriceDetails.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTIme);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // now we have to do the work for the admin in order to identify the specific user
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhoneNo())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // now we gonna send the toast message to the user
                                                Toast.makeText(ProductDetailsActivity.this, "Added To Cart List", Toast.LENGTH_SHORT).show();
                                                // after the product in the cart list now we gonna send the user to the homeActivity
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(final String productID) {
        //creating he database ref for the products
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);
                    // now we gonna set the details
                    tvProductNameDetails.setText(products.getPname());
                    tvProductPriceDetails.setText(products.getPrice());
                    tvProductDesDetails.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(ivProductImageDetails);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkOrderStatus(){

        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingStatus = dataSnapshot.child("status").getValue().toString();


                    if(shippingStatus.equals("shipped")){

                        status = "Order Shipped";

                    }else if(shippingStatus.equals("not shipped")){

                        status = "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

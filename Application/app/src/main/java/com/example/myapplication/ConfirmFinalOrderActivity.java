package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private Button btnConfirmOrder;
    private EditText edShipmentName,edShipmentPhoneNo,edShipmentAddress,edShipmentCity;
    private  String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price"); // receiving intent from cartActivity
        Toast.makeText(this, "Total Price = " + totalAmount, Toast.LENGTH_LONG).show();

        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        edShipmentName = findViewById(R.id.edShipmentName);
        edShipmentPhoneNo = findViewById(R.id.edShipmentPhoneNo);
        edShipmentAddress = findViewById(R.id.edShipmentAddress);
        edShipmentCity = findViewById(R.id.edShipmentCity);

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // before confimation of the code first we gonna check whether the provided information is Null or not ,if all are not null only then we proceed
                // for this we gonna create a function
                check();
            }
        });

    }

    private void check() {
        if(TextUtils.isEmpty(edShipmentName.getText().toString())){
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(edShipmentPhoneNo.getText().toString())){
            Toast.makeText(this, "Please provide your phone No", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(edShipmentAddress.getText().toString())){
            Toast.makeText(this, "Please provide your Complete  Address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(edShipmentCity.getText().toString())){
            Toast.makeText(this, "Please provide your City name", Toast.LENGTH_SHORT).show();
        }else{
            // if everything is ok
            confirmOrder();
        }
    }


    private void confirmOrder() {

        final String saveCurrentDate,saveCurrentTIme;
        // here we need to get the date and time
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTIme = currentDate.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",edShipmentName.getText().toString());
        ordersMap.put("phoneNo",edShipmentPhoneNo.getText().toString());
        ordersMap.put("address",edShipmentAddress.getText().toString());
        ordersMap.put("city",edShipmentCity.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTIme);
        ordersMap.put("status","not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // now we gonna empty the task of the current user
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhoneNo())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // here we gonna nofify the user that order is placed successfully
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}











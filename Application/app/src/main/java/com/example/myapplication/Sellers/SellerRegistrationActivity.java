package com.example.myapplication.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Customer.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button btnRegisterSeller,btnLogInSeller;
    private EditText edSellerName,edSellerPhoneNo,edSellerEmail,edSellerPassword,edSellerAddress;

    private FirebaseAuth mAuth ;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        btnRegisterSeller = findViewById(R.id.btnRegisterSeller);
        btnLogInSeller = findViewById(R.id.btnLogInSeller);

        edSellerName = findViewById(R.id.edSellerName);
        edSellerPhoneNo = findViewById(R.id.edSellerPhoneNo);
        edSellerEmail = findViewById(R.id.edSellerEmail);
        edSellerPassword = findViewById(R.id.edSellerPassword);
        edSellerAddress = findViewById(R.id.edSellerAddress);

        btnLogInSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this,SellerSignInActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here we gonna create a method
                registerSeller();
            }
        });
    }


    private void registerSeller() {
        final String name = edSellerName.getText().toString();
        final String phone = edSellerPhoneNo.getText().toString();
        final String email = edSellerEmail.getText().toString();
        final String password = edSellerPassword.getText().toString();
        final String address = edSellerAddress.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")){

            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                // now we goona save the info in the firebase realtime database
                                final DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();

                                String sid = mAuth.getCurrentUser().getUid();

                                //creating a hashmap for storing the info in the databse
                                HashMap<String,Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);
                                sellerMap.put("password",password);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    loadingBar.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this, "You have registered as Seller Successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomepageActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        }
                    });

        }
        else{
            Toast.makeText(this, "Please enter the required fields correctly", Toast.LENGTH_SHORT).show();
        }


    }
}

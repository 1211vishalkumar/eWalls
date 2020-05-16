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

public class SellerSignInActivity extends AppCompatActivity {

    private EditText edSellerEmailSignIn,edSellerPasswordSignIn;
    private Button btnLogInSellerSignIn;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sign_in);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        edSellerEmailSignIn = findViewById(R.id.edSellerEmailSignIn);
        edSellerPasswordSignIn = findViewById(R.id.edSellerPasswordSignIn);

        btnLogInSellerSignIn = findViewById(R.id.btnLogInSellerSignIn);

        btnLogInSellerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInSeller();
            }
        });
    }

    private void logInSeller() {

        final String email = edSellerEmailSignIn.getText().toString();
        final String password = edSellerPasswordSignIn.getText().toString();

        if(!email.equals("") && !password.equals("") ) {

            loadingBar.setTitle(" Seller Account SignIn");
            loadingBar.setMessage("please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loadingBar.dismiss();
                                // now we gonna send the seller to the Sellerhome Activity

                                Intent intent = new Intent(SellerSignInActivity.this, SellerHomepageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(SellerSignInActivity.this, "Enter the correct credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else{

            Toast.makeText(this, " Please fill the required fields !!", Toast.LENGTH_SHORT).show();

        }

    }
}

package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    EditText edSignInPhnNo,edSignInPassword;
    Button btnSignIn;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;
    private TextView tvAdminPanel,tvNotAdminPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edSignInPassword = findViewById(R.id.edSignInPassword);
        edSignInPhnNo = findViewById(R.id.edSignInPhnNo);

        btnSignIn = findViewById(R.id.btnSignIn);

        loadingBar = new ProgressDialog(this);

        tvAdminPanel = findViewById(R.id.tvAdminPanel);
        tvNotAdminPanel = findViewById(R.id.tvNotAdminPanel);


        chkBoxRememberMe =(CheckBox) findViewById(R.id.chkBoxRememberMe);
        Paper.init(this); // initializing the paper db

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we gonna create a function login user
                signInUser();

            }
        });

        tvAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setText("SignIn As Admin");
                tvAdminPanel.setVisibility(v.INVISIBLE);
                tvNotAdminPanel.setVisibility(v.VISIBLE);

                // now we gonna change the parentDbName for the Admin previously it was for the Users
                parentDbName = "Admins";
            }
        });

        // if the user presses the Admin button and if he wants to come back as not a admin
        tvNotAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setText("SignIn ");
                tvAdminPanel.setVisibility(v.VISIBLE);
                tvNotAdminPanel.setVisibility(v.INVISIBLE);

                // now we gonna change the parentDbName for the Users previously it has been changed to the Admin
                parentDbName = "Users";
            }
        });
    }

    private void signInUser() {
        // inside we gonna get the phone number and password
        String phoneNo = edSignInPhnNo.getText().toString();
        String password = edSignInPassword.getText().toString();

        if(TextUtils.isEmpty(phoneNo)){
            // show the toast message
            Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            // show the toast message
            Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }else{
            // we gonna allow the user to login
            loadingBar.setTitle("SignIn Account");
            loadingBar.setMessage("please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // now we gonna create a method in which we gonna allow access to user
            allowAccessToAccount(phoneNo,password);
        }
    }

    private void allowAccessToAccount(final String phoneNo, final String password) {

        //before allowing the access to user we gonna store the value to the prevalent class
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phoneNo);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        // now we need database ref
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        // retrieving th user
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(phoneNo).exists()){
                    // then we allow the user to SignIn

                    // retrieving the name and password ,since we gonna do this many times so we gonna make a new Package
                    Users usersData = dataSnapshot.child(parentDbName).child(phoneNo).getValue(Users.class);
                    // after passing the phone Number to Users class now we gonna retrieve the user data

                    if (usersData.getPhoneNo().equals(phoneNo)){
                        // now the phone no is correct so now we gonna check the password
                        if(usersData.getPassword().equals(password)){


                            if(parentDbName.equals("Admins")){
                                // then we gonna allow the Admins to access to its account
                                Toast.makeText(SignInActivity.this,"Welcome Admin you are signed In Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                // now sending the user to homeActivity
                                Intent intent = new Intent(SignInActivity.this,AdminAddingNewProductActivity.class);
                                startActivity(intent);
                            }else if(parentDbName.equals("Users")){
                                // then we gonna allow the user to access to its account
                                Toast.makeText(SignInActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                // now sending the user to homeActivity
                                Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }

                        }else{
                            //dissmiss the loading bar
                            loadingBar.dismiss();
                            // if the password is incorrect
                            Toast.makeText(SignInActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    // we gonna tell the user to create a new account
                    Toast.makeText(SignInActivity.this,"Account with this " +phoneNo +" does not exist ",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    // suggestion to user to create new account or check the credentials
                    Toast.makeText(SignInActivity.this,"you need to create a new account or check the input credentials",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button signIn,signUp;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);

        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey != "" && userPasswordKey != ""){
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){
                AllowAccess(userPhoneKey,userPasswordKey);

                loadingBar.setTitle("Already Signed In");
                loadingBar.setMessage("please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String phoneNo, final String password) {
        // now we need database ref
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        // retrieving th user
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phoneNo).exists()){
                    // then we allow the user to SignIn

                    // retrieving the name and password ,since we gonna do this many times so we gonna make a new Package
                    Users usersData = dataSnapshot.child("Users").child(phoneNo).getValue(Users.class);
                    // after passing the phone Number to Users class now we gonna retrieve the user data

                    if (usersData.getPhoneNo().equals(phoneNo)){
                        // now the phone no is correct so now we gonna check the password
                        if(usersData.getPassword().equals(password)){
                            // then we gonna allow the user to access to its account
                            Toast.makeText(MainActivity.this,"Please : You are already logged in",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            // now sending the user to homeActivity
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);

                        }else{
                            //dissmiss the loading bar
                            loadingBar.dismiss();
                            // if the password is incorrect
                            Toast.makeText(MainActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    // we gonna tell the user to create a new account
                    Toast.makeText(MainActivity.this,"Account with this " +phoneNo +" does not exist ",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    // suggestion to user to create new account or check the credentials
                    Toast.makeText(MainActivity.this,"you need to create a new account or check the input credentials",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

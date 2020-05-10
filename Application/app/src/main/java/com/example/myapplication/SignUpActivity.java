package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignUp;
    EditText edSignUpPassword,edSignUpPhnNo,edUserName;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp);

        edSignUpPassword = findViewById(R.id.edSignUpPassword);
        edSignUpPhnNo = findViewById(R.id.edSignUpPhnNo);
        edUserName = findViewById(R.id.edUserName);

        loadingBar = new ProgressDialog(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount(){
        String name = edUserName.getText().toString();
        String phoneNo = edSignUpPhnNo.getText().toString();
        String password = edSignUpPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            // show the toast message
            Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNo)){
            // show the toast message
            Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            // show the toast message
            Toast.makeText(this,"Please enter valid password",Toast.LENGTH_SHORT).show();
        }else{
            // if everything is ok ,now we gonna store the data in database
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //after doing this we gonna validate the phone number whether the no is already available in the database or not
            // and if this no is not available in the databse then we gonna create a new account
            // so we gonna make a fuction to validate the phonr number
            validatePhoneNumber(name,phoneNo,password);
        }
    }

    private void validatePhoneNumber(final String name, final String phoneNo, final String password) {
        // now we gonna create a database reference
        // creating the instance of database
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        // now we gonna validate
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // hare we are using the phone no as the primary key
                if(!(dataSnapshot.child("Users").child(phoneNo).exists())){
                    // if this phone no does not exist then we gonna create a user account

                    HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("phoneNo",phoneNo);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);

                    rootRef.child("Users").child(phoneNo).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // if the task is successful
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this,"Congo your account has beem created",Toast.LENGTH_SHORT).show();
                                        // then dismiss the loading bar
                                        loadingBar.dismiss();

                                        // once the account has been created successfully then send the user to SignActivity to sign into the account
                                        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                        startActivity(intent);
                                    }else{

                                        //dismiss the loading bar
                                        loadingBar.dismiss();
                                        // if there is any unknown failure such as no network connection
                                        Toast.makeText(SignUpActivity.this,"Network Error : please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    // we gonna tell that this phone no already exists
                    Toast.makeText(SignUpActivity.this,"This "+ phoneNo +"already exists",Toast.LENGTH_SHORT).show();

                    // now e gonna dismiss the loading bar
                    loadingBar.dismiss();

                    Toast.makeText(SignUpActivity.this,"Please try again using another phone number",Toast.LENGTH_SHORT).show();

                    // now send the user to the main Activity through intent

                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

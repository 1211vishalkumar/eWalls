package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check =""; // in order to receive he intent
    private TextView tvPageTitleResetPassword,tvTitleQuestionsResetPassword;
    private EditText edPhoneNoResetActivity,edQuestionResetPassword1,edQuestionResetPassword2;
    private Button btnVerifyResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        tvPageTitleResetPassword = findViewById(R.id.tvPageTitleResetPassword);
        tvTitleQuestionsResetPassword = findViewById(R.id.tvTitleQuestionsResetPassword);

        edPhoneNoResetActivity = findViewById(R.id.edPhoneNoResetActivity);
        edQuestionResetPassword1 = findViewById(R.id.edQuestionResetPassword1);
        edQuestionResetPassword2 = findViewById(R.id.edQuestionResetPassword2);

        btnVerifyResetPassword = findViewById(R.id.btnVerifyResetPassword);



    }

    @Override
    protected void onStart() {
        super.onStart();

        edPhoneNoResetActivity.setVisibility(View.GONE);

        // here we gonna differntiate whether the user has come here from the SignIn Activity or the Setting Activity
        if(check.equals("settings")){

            //then we goona change the fields accordingly
            tvPageTitleResetPassword.setText("Set Security Questions");
            tvTitleQuestionsResetPassword.setText("Please Answer the following Security Questions");
            btnVerifyResetPassword.setText("Save ");

            displayPreviousAnswers();

            btnVerifyResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswers();

                }
            });


        }else if(check.equals("signIn")){
            // if the user forget the password

            edPhoneNoResetActivity.setVisibility(View.VISIBLE);

        }
    }

    private void setAnswers(){
        // here the user will anwer question1 and q 2
        String answer1 = edQuestionResetPassword1.getText().toString().toLowerCase();
        String answer2 = edQuestionResetPassword2.getText().toString().toLowerCase();

        if(edQuestionResetPassword1.equals("")){

            Toast.makeText(ResetPasswordActivity.this, "Please answer this Q .", Toast.LENGTH_SHORT).show();

        }else if(edQuestionResetPassword2.equals("")){

            Toast.makeText(ResetPasswordActivity.this, "Please answer this Q .", Toast.LENGTH_SHORT).show();

        }else{
            // here we gonna do our task
            //here we need to create a database reference
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhoneNo());

            HashMap<String,Object> userDataMap = new HashMap<>();
            userDataMap.put("answer1",answer1);
            userDataMap.put("answer2",answer2);

            ref.child("Security Questions").updateChildren(userDataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // now we goona tell the user that security questions set successfully
                                Toast.makeText(ResetPasswordActivity.this, "Security questions saved successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

    // in this method we retrive the previous Answers answered bythe user
    private void displayPreviousAnswers(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    edQuestionResetPassword1.setText(ans1);
                    edQuestionResetPassword2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

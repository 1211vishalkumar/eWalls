package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddingNewProductActivity extends AppCompatActivity {

    private String categoryName ,description,Pname,price,saveCurrDate,saveCurrTime;
    private Button btnAddNewProduct;
    private ImageView ivSelectProductImage;
    private EditText edProductName,edProductDescription,edProductPrice;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_adding_new_product);

        Toast.makeText(AdminAddingNewProductActivity.this,"Welcome Admin",Toast.LENGTH_SHORT).show();

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);

        ivSelectProductImage = findViewById(R.id.ivSelectProductImage);

        edProductName = findViewById(R.id.edProductName);
        edProductDescription = findViewById(R.id.edProductDescription);
        edProductPrice = findViewById(R.id.edProductPrice);

        loadingBar = new ProgressDialog(this);

        ivSelectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });


    }



    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == galleryPick && resultCode == RESULT_OK && data!=null){
            // now we gonna get image uri
            imageUri = data.getData();
            // now e need to display this image on our imageview
            ivSelectProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        // here we need to get description,name,price;
        description = edProductDescription.getText().toString();
        Pname = edProductName.getText().toString();
        price = edProductPrice.getText().toString();

        // now e gonna veify
        if(imageUri == null){
            Toast.makeText(this,"Product Image is mandatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(description)){
            if(imageUri == null){
                Toast.makeText(this,"Product description is mandatory",Toast.LENGTH_SHORT).show();
            }
        }
        else if(TextUtils.isEmpty(price)){
            if(imageUri == null){
                Toast.makeText(this,"Product price is mandatory",Toast.LENGTH_SHORT).show();
            }
        }
        else if(TextUtils.isEmpty(Pname)){
            if(imageUri == null){
                Toast.makeText(this,"Product name is mandatory",Toast.LENGTH_SHORT).show();
            }
        }else{
            // if everything goes well now we gonna add these data in firebase database
            // now we gonna crete a method that gonna save image in firebase
            storeProductInfo();
        }
    }

    private void storeProductInfo() {

        loadingBar.setTitle("Add new Product");
        loadingBar.setMessage("please wait while we are adding new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // creating a calendar
        Calendar calendar = Calendar.getInstance();
        //getting the current date
        SimpleDateFormat currDate = new SimpleDateFormat("MMM dd,YYYY");
        saveCurrDate = currDate.format(calendar.getTime());

        //getting the current time
        SimpleDateFormat currTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrTime = currTime.format(calendar.getTime());

        // since there we be millions of product in this app and for each product info we need a random key so that none of our
        // product info get refreshed with another product info,so create a random key by combining the cuurTime and currDate because this goona always be unique
        // we are not using firebase pushback method which also creates a random key
        productRandomKey = saveCurrDate + saveCurrTime ;

        //now we gonna frst store the imageuri/ productImage inside the firebase  storage
        // the we will be albe to store the link of that image inside the firebase RealTImeDatabaseStorage
        // ansd we gonna diaplay this to the user

        // creating the reference for firebase storage
        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        // if any failure occurs
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddingNewProductActivity.this, "Error"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddingNewProductActivity.this, "Product Image uploaded Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        // once this product is successfully uploaded in the storage now we have to get the link in the firebase Database to disaplay this to the user
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    // if the task is not successful then we gonna draw the exception
                    throw  task.getException();

                }
                downloadImageUrl = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){

                    downloadImageUrl = task.getResult().toString();

                    Toast.makeText(AdminAddingNewProductActivity.this, " got Product Image Url Successfully", Toast.LENGTH_SHORT).show();

                    // once we got the image url now we gonna store all the info of the new product inside the firebase database
                    // and for this we are creating the another method
                    saveProductInfoToDatabase();
                }
            }
        });


    }

    private void saveProductInfoToDatabase() {

        HashMap<String,Object> productMap  = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrDate);
        productMap.put("time",saveCurrTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("pname",Pname);

        // now we gonna create database ref  and for this we gonna create another inside the firebase database for the products
        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // once the product is added successfully we can send the admin to  AdminCatlogActivity
                            // where admin can add more products if admin wants

                            Intent intent = new Intent(AdminAddingNewProductActivity.this, AdminCatlogActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddingNewProductActivity.this, "Product is added successfully ", Toast.LENGTH_SHORT).show();
                        }else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddingNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}

package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckAdminNewProductsActivity extends AppCompatActivity {

    private RecyclerView rvAdminCheckProductsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_admin_new_products);

        unVerifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        rvAdminCheckProductsList = findViewById(R.id.rvAdminCheckProductsList);
        rvAdminCheckProductsList.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        rvAdminCheckProductsList.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //inside this we gonna start our firebase query
        FirebaseRecyclerOptions<Products> options
                = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unVerifiedProductsRef.orderByChild("productStatus").equalTo("not Approved"),Products.class)
                .build();

        // now we gonna use Firebase recycler Adapter inorder to popoulate the recylerView
        // here the second parameter is static class
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        holder.tvProdName.setText(model.getPname());
                        holder.tvProdDescription.setText(model.getDescription());
                        holder.tvProdPrice.setText("Price (in Rs) = " + model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.ivProdImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // herw we gonna dispaly our dialog box in which wew gonna show Admin 2 options
                                final String productID = model.getPid();
                                CharSequence options[] = new CharSequence[] {"Yes","No" };

                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckAdminNewProductsActivity.this);
                                builder.setTitle("You want to approve this prooduct or not");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if( i == 0){
                                            //Admin has clicked on Yes
                                            // now we have to change the product state to approve
                                            // for this we gonna create a method
                                            changeProductStatus(productID);
                                        }
                                        else if( i==1 ){
                                            //Admin has clicked on No


                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        // acessing the product item layout here
                        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        rvAdminCheckProductsList.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeProductStatus(String productID) {

        // we need a database ref
        unVerifiedProductsRef.child(productID).child("productStatus").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(CheckAdminNewProductsActivity.this, "This item has been approved and is available for the customers", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

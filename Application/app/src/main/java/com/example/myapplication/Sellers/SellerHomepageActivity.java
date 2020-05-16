package com.example.myapplication.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Admin.CheckAdminNewProductsActivity;
import com.example.myapplication.Customer.MainActivity;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.ItemViewHolder;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// in this we gonna show all the products of this specific seller
public class SellerHomepageActivity extends AppCompatActivity {

    private TextView mTextView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(SellerHomepageActivity.this, SellerHomepageActivity.class);
                    startActivity(intentHome);
                    return true;

                case R.id.navigation_add:
                    Intent intentCat = new Intent(SellerHomepageActivity.this, SellerProductCatlogActivity.class);
                    startActivity(intentCat);
                    return true;

                /*case R.id.navigation_notifications:
                    mTextView.setText("");
                    return true;*/

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intentMain = new Intent(SellerHomepageActivity.this, MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();
                    return true;
            }
            return false;
        }
    };

    private RecyclerView rvSellerHomepageUnverifiedProductList;
    private TextView tvSellerHomeTitle;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedSellerProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_homepage);

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        unverifiedSellerProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        rvSellerHomepageUnverifiedProductList = findViewById(R.id.rvSellerHomepageUnverifiedProductList);
        rvSellerHomepageUnverifiedProductList.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        rvSellerHomepageUnverifiedProductList.setLayoutManager(layoutManager);

        tvSellerHomeTitle = findViewById(R.id.tvSellerHomeTitle);


        /*// Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        //inside this we gonna start our firebase query
        FirebaseRecyclerOptions<Products> options
                = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedSellerProductRef.orderByChild("sellerID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                .build();

        // now we gonna use Firebase recycler Adapter inorder to popoulate the recylerView
        // here the second parameter is static class
        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model) {

                        holder.tvProdNameSellerItemView.setText(model.getPname());
                        holder.tvProdDescriptionSellerItemView.setText(model.getDescription());
                        holder.tvProdPriceSellerItemView.setText("Price (in Rs) = " + model.getPrice());
                        holder.tvProdStatusSellerItemView.setText(model.getProductStatus());

                        Picasso.get().load(model.getImage()).into(holder.ivProdImageSellerItemView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // herw we gonna dispaly our dialog box in which wew gonna show Admin 2 options
                                final String productID = model.getPid();
                                CharSequence options[] = new CharSequence[] {"Yes","No" };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomepageActivity.this);
                                builder.setTitle("You want to Delete this product or not");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if( i == 0){
                                            //Sellee has clicked on Yes
                                            // now we have to change the product state to approve
                                            // for this we gonna create a method
                                            deleteProduct(productID);
                                        }
                                        else if( i==1 ){
                                            //Sller has clicked on No


                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        // acessing the product item layout here
                        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_itemview_products,parent,false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        rvSellerHomepageUnverifiedProductList.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productID) {

        // we need a database ref
        unverifiedSellerProductRef.child(productID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(SellerHomepageActivity.this, "This item has been deleted successfullt", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}





package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Products;
import com.example.myapplication.Prevalent.Prevalent;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;
    //creating dtabse reference

    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here we gonna send the user to the cart Activity
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
                );

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing our header view  with name of user and its image
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserProfileName = headerView.findViewById(R.id.tvUserProfileName);
        CircleImageView userProfileImage = headerView.findViewById(R.id.userProfileImage);

        tvUserProfileName.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(userProfileImage);

        recyclerView = findViewById(R.id.rvMenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
/*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
*/
/*       NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        // adding query to retrive all the products
        FirebaseRecyclerOptions<Products> options = new  FirebaseRecyclerOptions.Builder <Products>()
                .setQuery(productRef,Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.tvProdName.setText(model.getPname());
                        holder.tvProdDescription.setText(model.getDescription());
                        holder.tvProdPrice.setText("Price (in Rs) = " + model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.ivProdImage);

                        // now we gonna set a click listener on a item view in order to open ProductDetailsActivity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
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

        //now we need to populate our recycler view
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle action bar item click here.The action bar will automatically
        // handle clicks on the Home/up button, so long
        // as you specify a parent activity in AndroidManifest
        int id = item.getItemId();

        //no need of this as we are already using setting option in navigation drawer
        //noinspection SimplifiableIfStatement
        /*if(id == R.id.action_settings){
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //handle navigation view items clicks here
        int id = item.getItemId();

        if(id == R.id.nav_cart){
            Intent intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }else if( id == R.id.nav_settings){
            Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
            startActivity(intent);

        }else if( id == R.id.nav_orders){



        }else if( id == R.id.nav_categories){



        }else if(id == R.id.nav_search){

            Intent intent = new Intent(HomeActivity.this,SearchProductsActivity.class);
            startActivity(intent);
        }
        else if( id == R.id.nav_logout){
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}

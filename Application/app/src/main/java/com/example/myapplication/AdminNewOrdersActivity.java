package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrdersList;
    private DatabaseReference ordersRef;
    private DatabaseReference cartListRef;              //  <-

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");  //  <-

        rvOrdersList = findViewById(R.id.rvOrdersList);
        rvOrdersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        // here we gonna retrive and display the orders and we will be using firebase Recycler Adapter
        // for this we setting up the query
        FirebaseRecyclerOptions<AdminOrders> options
                = new  FirebaseRecyclerOptions.Builder <AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter
                = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                // in this we gonna display the values on our fields of Orders_layout
                holder.tvUserNameOrder.setText("Name :" + model.getName());
                holder.tvUserPhoneNoOrder.setText("Phone :" + model.getPhoneNo());
                holder.tvUserTotalPriceOrder.setText("Total Amount :" + model.getTotalAmount());
                holder.tvUserShippingAddressAndCityOrder.setText("Shipping Address :" + model.getAddress() + " ," + model.getCity());
                holder.tvUserDateAndTimeOrder.setText("Ordered At :" + model.getDate() + "  " + model.getTime());

                holder.btnShowAllProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID = getRef(position).getKey();

                        // now we send the intent to AdminUserProductsActivity
                        Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });

                //whenever the user click on one of the orders of the users on this ordersList
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{"Yes","No"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you Shipped this order products ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if( i == 0){
                                    //for yes
                                    //whenever the user clicks on Yes then we gonna remove that order from database from Orders node
                                    String uID = getRef(position).getKey();
                                    removeOrder(uID);

                                }else{
                                    //for no
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        rvOrdersList.setAdapter(adapter);
        adapter.startListening();
    }



    // here we are making the static class in the same Activity for FireBaseRecyclerOptions
    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        //here we gonna Access all the items of Orders_Layout (the same we can do  by making a New Activity in ViewHolder Package)
        public TextView tvUserNameOrder,tvUserPhoneNoOrder,tvUserTotalPriceOrder,tvUserShippingAddressAndCityOrder,tvUserDateAndTimeOrder;
        public Button btnShowAllProducts;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserNameOrder = itemView.findViewById(R.id.tvUserNameOrder);
            tvUserPhoneNoOrder = itemView.findViewById(R.id.tvUserPhoneNoOrder);
            tvUserTotalPriceOrder = itemView.findViewById(R.id.tvUserTotalPriceOrder);
            tvUserShippingAddressAndCityOrder = itemView.findViewById(R.id.tvUserShippingAddressAndCityOrder);
            tvUserDateAndTimeOrder = itemView.findViewById(R.id.tvUserDateAndTimeOrder);

            btnShowAllProducts = itemView.findViewById(R.id.btnShowAllProducts);


        }
    }


    private void removeOrder(String uID) {
            ordersRef.child(uID).removeValue();
            cartListRef.child("Admin View").child(uID).removeValue(); //               <-
            // in this we can also have to fix that admin view After Admin ship that object to the customer
    }

}














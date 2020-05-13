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
import android.widget.Toast;

import com.example.myapplication.Model.Cart;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    RecyclerView rvCartList;
    Button btnNextProcess;
    TextView tvTotalPriceCartList,tvCartMessage1;
    RecyclerView.LayoutManager layoutManager;
    private int sumTotalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnNextProcess = findViewById(R.id.btnNextProcess);

        rvCartList = findViewById(R.id.rvCartList);
        rvCartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvCartList.setLayoutManager(layoutManager);

        tvTotalPriceCartList = findViewById(R.id.tvTotalPriceCartList);
        tvCartMessage1 = findViewById(R.id.tvCartMessage1);

        btnNextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //diaplaying the total price on the textView
                tvTotalPriceCartList.setText("Total Price = "+ String.valueOf(sumTotalPrice));

                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(sumTotalPrice));
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderStatus();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo()).child("Products"),Cart.class)
                .build();

        // now we are ready to use firebase recycler Adapter
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                // in this we gonna display the things
                holder.tvProdQuantityCartItem.setText("Quantity = " + model.getQuantity());
                holder.tvProdPriceCartItem.setText("Price = " + model.getPrice());
                holder.tvProdNameCartItem.setText( model.getPname());

                //here we gonna claculate the sum total price
                int individualProductTotalPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                sumTotalPrice = sumTotalPrice + individualProductTotalPrice;



                // when the user click on any cart product
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // after clicking we gonna give 2 options to the user edit or remove
                        CharSequence options[] = new CharSequence[]{"Edit","Remove"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        // now we gonna set click listener on these 2 buttons
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                    if(i==0){
                                        // i.e if the yser clicks on the edit button
                                        // now we gonna send the user to product detail Activity
                                        Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                        intent.putExtra("pid",model.getPid());
                                        startActivity(intent);
                                    }
                                    if(i==1){
                                        cartListRef.child("User View")
                                                .child(Prevalent.currentOnlineUser.getPhoneNo())
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(CartActivity.this, "Item removed from your Cart", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                            }
                        });
                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        rvCartList.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderStatus(){

        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingStatus = dataSnapshot.child("status").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if(shippingStatus.equals("shipped")){

                        tvTotalPriceCartList.setText("Dear " + userName + "\n order is shipped successfully");
                        rvCartList.setVisibility(View.GONE);
                        // now we have to visible the message
                        tvCartMessage1.setVisibility(View.VISIBLE);
                        tvCartMessage1.setText("Congratulation Your Order has been Shipped successfully,soon you will receive the order at your doorstep");
                        // now we also to diable the nextproceesButton
                        btnNextProcess.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once your receive your current placed Order ", Toast.LENGTH_SHORT).show();


                    }else if(shippingStatus.equals("not shipped")){
                        tvTotalPriceCartList.setText("Shipping status = not Shipped yet");
                        rvCartList.setVisibility(View.GONE);
                        // now we have to visible the message
                        tvCartMessage1.setVisibility(View.VISIBLE);
                        // now we also to diable the nextproceesButton
                        btnNextProcess.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once your receive your current placed Order ", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}

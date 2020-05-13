package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // here we gonna access the item of cart_item_layout
    public TextView tvProdNameCartItem,tvProdQuantityCartItem,tvProdPriceCartItem;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        tvProdNameCartItem = itemView.findViewById(R.id.tvProdNameCartItem);
        tvProdQuantityCartItem = itemView.findViewById(R.id.tvProdQuantityCartItem);
        tvProdPriceCartItem = itemView.findViewById(R.id.tvProdPriceCartItem);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }
}

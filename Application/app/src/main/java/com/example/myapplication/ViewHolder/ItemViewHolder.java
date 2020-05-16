package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // here we gonna access seller_itemview_product and its fields

    public TextView tvProdDescriptionSellerItemView,tvProdNameSellerItemView,tvProdPriceSellerItemView,tvProdStatusSellerItemView;
    public ImageView ivProdImageSellerItemView;
    public ItemClickListener listener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        tvProdDescriptionSellerItemView = itemView.findViewById(R.id.tvProdDescriptionSellerItemView);
        tvProdNameSellerItemView = itemView.findViewById(R.id.tvProdNameSellerItemView);
        tvProdPriceSellerItemView = itemView.findViewById(R.id.tvProdPriceSellerItemView);
        tvProdStatusSellerItemView = itemView.findViewById(R.id.tvProdStatusSellerItemView);

        ivProdImageSellerItemView = itemView.findViewById(R.id.ivProdImageSellerItemView);

    }

    public void setitemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.OnClick(v,getAdapterPosition(),false);
    }
}

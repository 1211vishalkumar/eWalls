package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // here we gonna access product_item_layout and its fields

    public TextView tvProdDescription,tvProdName,tvProdPrice;
    public ImageView ivProdImage;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        tvProdDescription = itemView.findViewById(R.id.tvProdDescription);
        tvProdName = itemView.findViewById(R.id.tvProdName);
        tvProdPrice = itemView.findViewById(R.id.tvProdPrice);

        ivProdImage = (ImageView) itemView.findViewById(R.id.ivProdImage);


    }

    public void setitemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.OnClick(view,getAdapterPosition(),false);
    }
}

package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.itemClicklistener;

public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView product_name,product_desc,product_price;
    public ImageView product_img;
    public itemClicklistener listener;
    public viewHolder(@NonNull View itemView) {
        super(itemView);

        product_img=(ImageView)itemView.findViewById(R.id.product_image);
        product_name=(TextView)itemView.findViewById(R.id.product_name);
        //product_desc=(TextView)itemView.findViewById(R.id.product_description);
        product_price=(TextView)itemView.findViewById(R.id.product_price);

    }

    public void setitemclickListener(itemClicklistener listener) {
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}

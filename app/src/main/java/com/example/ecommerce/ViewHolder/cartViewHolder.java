package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.itemClicklistener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView price,quantity,name;
    private itemClicklistener itemClickListener;

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);

        name=itemView.findViewById(R.id.name);
        price=itemView.findViewById(R.id.price);
        quantity=itemView.findViewById(R.id.quantity);;

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(itemClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;

    }
}

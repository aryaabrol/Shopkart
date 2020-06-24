package com.example.ecommerce;

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

import com.example.ecommerce.Model.cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.cartViewHolder;
import com.example.ecommerce.prevelant.prevelant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkout;
    private TextView total;
    private int totalCost=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView= findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        checkout=(Button) findViewById(R.id.checkout);
        total=(TextView) findViewById(R.id.totalCost);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(cartActivity.this,shipping.class);
                intent.putExtra("total",totalCost);
                startActivity(intent);
            }
        });


    }

    protected void onStart() {

        super.onStart();
        final DatabaseReference cartlistref=FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<cart> options=
                new FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(cartlistref.child("User View")
                .child(prevelant.currentUser.getPhone())
                        .child("Products"),cart.class)
                .build();

        FirebaseRecyclerAdapter<cart, cartViewHolder> adapter=new FirebaseRecyclerAdapter<cart, cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull final cart model) {
                holder.quantity.setText("Quantity: " + model.getQuantity());
                holder.name.setText(model.getName());
                holder.price.setText("Price: "+ model.getPrice()+" $");

                int price=(Integer.valueOf(model.getPrice()))*Integer.valueOf(model.getQuantity());
                totalCost=totalCost+price;
                total.setText("Total: "+String.valueOf(totalCost)+"$");



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            CharSequence options[]=new CharSequence[]{
                              "Edit","Remove"
                            };
                        AlertDialog.Builder builder=new AlertDialog.Builder(cartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                            if(i==0){
                                Intent intent=new Intent(cartActivity.this,details.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }

                            else if(i==1){
                                cartlistref.child("User View")
                                        .child(prevelant.currentUser.getPhone())
                                        .child("Products")
                                        .child(model.getPid())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(cartActivity.this,"Product removed successfully",Toast.LENGTH_LONG).show();
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
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem,parent,false);
                cartViewHolder holder=new cartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}

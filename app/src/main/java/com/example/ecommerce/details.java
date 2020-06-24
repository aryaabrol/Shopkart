package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.product;
import com.example.ecommerce.prevelant.prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class details extends AppCompatActivity {
    private ImageView image1;
    private TextView price1,name1,description1;
    private Button cart;
    private String id="";
    private ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        id=getIntent().getStringExtra("pid");

        price1=(TextView) findViewById(R.id.price2);
        name1=(TextView) findViewById(R.id.name);
        description1=(TextView) findViewById(R.id.description);
        image1=(ImageView) findViewById(R.id.new_image);
        cart=(Button) findViewById(R.id.cart);
        numberButton=(ElegantNumberButton) findViewById(R.id.number_button);

        getdetails(id);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String time,date;
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd,yyyy");
                date=currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss");
                time=currenttime.format(calendar.getTime());

                final DatabaseReference cartref= FirebaseDatabase.getInstance().getReference().child("Cart List");
                final HashMap<String,Object>productMap=new HashMap<>();
                productMap.put("pid",id);
                productMap.put("date",date);
                productMap.put("time",time);
                productMap.put("description",description1.getText().toString());
                productMap.put("price",price1.getText().toString());
                productMap.put("name",name1.getText().toString());
                productMap.put("quantity", numberButton.getNumber());

                cartref.child("User View").child(prevelant.currentUser.getPhone())
                        .child("Products").child(id)
                        .updateChildren(productMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                cartref.child("Admin View").child(prevelant.currentUser.getPhone())
                                        .child("Products").child(id)
                                        .updateChildren(productMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(details.this,"Added to cart",Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(details.this,homepage.class);
                                                startActivity(intent);
                                            }
                                        });

                                }

                            }
                        });


            }
        });

    }


    private void getdetails(String productid){
        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product products=dataSnapshot.getValue(product.class);

                name1.setText(products.getName());
                price1.setText(products.getPrice());
                description1.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(image1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

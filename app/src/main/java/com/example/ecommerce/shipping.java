package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.prevelant.prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class shipping extends AppCompatActivity {
    private EditText name,phone,address,city;
    private Button confirm;
    private String totalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        totalamount=getIntent().getStringExtra("Total");


        name=(EditText) findViewById(R.id.editText);
        phone=(EditText) findViewById(R.id.editText2);
        address=(EditText) findViewById(R.id.editText3);
        city=(EditText) findViewById(R.id.editText4);
        confirm=(Button) findViewById(R.id.button);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(shipping.this,"Enter name",Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(shipping.this,"Enter phone",Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(address.getText().toString())){
                    Toast.makeText(shipping.this,"Enter address",Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(city.getText().toString())){
                    Toast.makeText(shipping.this,"Enter city",Toast.LENGTH_SHORT).show();
                }

                else{
                    ConfirmOrder();
                }
            }
        });
    }

    private void ConfirmOrder(){
        final String time,date;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd,yyyy");
        date=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss");
        time=currenttime.format(calendar.getTime());

        final DatabaseReference orders=FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(prevelant.currentUser.getPhone());

        HashMap<String,Object> ordermap=new HashMap<>();
        ordermap.put("Total Amount",totalamount);
        ordermap.put("name",name.getText().toString());
        ordermap.put("address",address.getText().toString());
        ordermap.put("phone",phone.getText().toString());
        ordermap.put("city",city.getText().toString());
        ordermap.put("date",date);
        ordermap.put("time",time);
        ordermap.put("state","not shipped");

        orders.updateChildren(ordermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(prevelant.currentUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(shipping.this,"Order has been placed",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(shipping.this,homepage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
    }

}

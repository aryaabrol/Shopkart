package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class categoryActivity extends AppCompatActivity {
    private ImageView tShirt,sports,female,shoes,goggles,hats,earphones,laptops,phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tShirt=(ImageView) findViewById(R.id.teeshirt);
        sports=(ImageView) findViewById(R.id.sport);
        female=(ImageView) findViewById(R.id.female);
        shoes=(ImageView) findViewById(R.id.shoes);
        goggles=(ImageView) findViewById(R.id.glass);
        hats=(ImageView) findViewById(R.id.hats);
        earphones=(ImageView) findViewById(R.id.headphones);
        laptops=(ImageView) findViewById(R.id.laptop);
        phones=(ImageView) findViewById(R.id.phone);

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","tshirt");
                startActivity(intent);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","sportstshirt");
                startActivity(intent);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","female");
                startActivity(intent);
            }
        });

        goggles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","goggles");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });

        earphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","earphones");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","laptop");
                startActivity(intent);
            }
        });

        phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(categoryActivity.this,addThings.class);
                intent.putExtra("category","smartphones");
                startActivity(intent);
            }
        });



    }
}

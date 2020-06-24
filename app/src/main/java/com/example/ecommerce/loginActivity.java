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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.ecommerce.Model.Users;

public class loginActivity extends AppCompatActivity {
    private EditText phonenumber,password1;
    private Button login;
    private TextView adminlogin;
    String parentdb="Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phonenumber=(EditText) findViewById(R.id.pnumber);
        password1=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.logout);
        adminlogin=(TextView) findViewById(R.id.admin);

        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentdb="Admins";
                login.setText("Login Admin");
                adminlogin.setVisibility(View.INVISIBLE);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String number= phonenumber.getText().toString();
        String password= password1.getText().toString();

        if(TextUtils.isEmpty(number)) {
            Toast.makeText(getApplicationContext(), "please enter phone number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "please enter password ", Toast.LENGTH_SHORT).show();
        }

        else{
            accessacc(number,password);
        }
    }

    private void accessacc(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child(parentdb).child(phone).exists())){
                    Toast.makeText(getApplicationContext(), "Account does not exist", Toast.LENGTH_SHORT).show();
                }

                else{
                    Users user= dataSnapshot.child(parentdb).child(phone).getValue(Users.class);
                    if(user.getPhone().equals(phone)) {
                        if(user.getPassword().equals(password)) {

                            if(parentdb.equals("Admins")){
                            Toast.makeText(getApplicationContext(), "Admin Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent  =new Intent(loginActivity.this,categoryActivity.class);
                            startActivity(intent);}

                            else{
                                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent  =new Intent(loginActivity.this,homepage.class);
                                prevelant.currentUser=user;
                                startActivity(intent);

                            }

                        }
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

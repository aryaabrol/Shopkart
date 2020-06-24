package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class join extends AppCompatActivity {
    private EditText name,phone,password;
    private Button register;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        register= (Button) findViewById(R.id.joinbutton);
        name=(EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone);
        password=(EditText) findViewById(R.id.pass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }

            private void CreateAccount() {
                String n= name.getText().toString();
                String p= phone.getText().toString();
                String pass= password.getText().toString();

                if(TextUtils.isEmpty(n)) {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(p)) {
                    Toast.makeText(getApplicationContext(), "please enter phone number", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "please enter password", Toast.LENGTH_SHORT).show();
                }

                else{
                    dialog = new ProgressDialog(join.this);
                    dialog.setMessage("Please wait while we are checking the credentials");
                    dialog.show();

                    //loading.setTitle("Create Account");
                    //loading.setMessage("Please wait while we are checking the credentials");
                    //loading.setCanceledOnTouchOutside(false);
                    //loading.show();

                    Validate(n,p,pass);

                }
            }

            private void Validate(final String n, final String p, final String pass) {
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!(dataSnapshot.child("Users").child(p).exists())){
                            HashMap<String,Object> userdataMap=new HashMap<>();
                            userdataMap.put("phone",p);
                            userdataMap.put("password",pass);
                            userdataMap.put("name",n);

                            RootRef.child("Users").child(p).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            Intent intent  =new Intent(join.this,loginActivity.class);
                                            startActivity(intent);
                                        }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "phone number exists", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}

package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addThings<pname> extends AppCompatActivity {
    private String categoryName, pname,pprice,pdesc,savecurrentDate,savecurrentTime,key,downLoadImageURL;
    private EditText name,desc,price;
    private Button add;
    private ImageView icon;
    private static final int GalleryPick=1;
    private Uri imageUri;
    private StorageReference productImages;
    private DatabaseReference productref;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_things);

        name=(EditText)findViewById(R.id.name);
        desc=(EditText)findViewById(R.id.desc);
        price=(EditText)findViewById(R.id.price);
        add=(Button)findViewById(R.id.add);
        icon=(ImageView)findViewById(R.id.icon) ;

        categoryName= getIntent().getExtras().get("category").toString();
        productImages= FirebaseStorage.getInstance().getReference().child("Product Images");
        productref= FirebaseDatabase.getInstance().getReference().child("Products");
        //Toast.makeText(getApplicationContext(), categoryName, Toast.LENGTH_SHORT).show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validatedata();
            }
        });



        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }

        });
    }

    private void OpenGallery(){
        Intent gallery=new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            icon.setImageURI(imageUri);

        }
    }

    private void Validatedata(){
        dialog = new ProgressDialog(addThings.this);
        dialog.setMessage("Please wait while we are adding the details");
        dialog.show();

        pname=name.getText().toString();
        pdesc=desc.getText().toString();
        pprice=price.getText().toString();

        if (imageUri==null){
            Toast.makeText(getApplicationContext(), "please upload an image", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(pprice)) {
            Toast.makeText(getApplicationContext(), "please add price", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(pname)){
        Toast.makeText(getApplicationContext(), "please add name", Toast.LENGTH_SHORT).show();
        }

        else{
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
            savecurrentDate=currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
            savecurrentTime=currentTime.format(calendar.getTime());

            key=savecurrentDate+savecurrentTime;

            final StorageReference filepath;
            filepath = productImages.child(imageUri.getLastPathSegment());
            final UploadTask uploadTask=filepath.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "image uploaded successfully", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        downLoadImageURL=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                downLoadImageURL=task.getResult().toString();
                                //Toast.makeText(getApplicationContext(), "url added ", Toast.LENGTH_SHORT).show();
                                saveTodatabase();
                            }
                        }
                    });
                }
            });
        }
    }
    private void saveTodatabase(){
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",key);
        productMap.put("date",savecurrentDate);
        productMap.put("time",savecurrentTime);
        productMap.put("description",pdesc);
        productMap.put("image",downLoadImageURL);
        productMap.put("category",categoryName);
        productMap.put("price",pprice);
        productMap.put("name",pname);

        productref.child(key).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Added successsfully", Toast.LENGTH_SHORT).show();
                            Intent intent  =new Intent(addThings.this,categoryActivity.class);
                            startActivity(intent);

                        }
                    }
                });


    }
}

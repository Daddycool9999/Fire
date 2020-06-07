package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class Feed extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn=getMenuInflater();
        mn.inflate(R.menu.second,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                Intent kk=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(kk);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));

        }
        Intent intent = getIntent();
        final String str= intent.getStringExtra("UID");

        final ListView lv=findViewById(R.id.lv);
        final ArrayList<String> arr=new ArrayList<String>();
        final LinearLayout lin=findViewById(R.id.lin);
        Log.i("FEED:",str+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        //Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("Images").child(str).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                FirebaseStorage.getInstance().getReference().child("Images").child(str).child(dataSnapshot.getValue().toString()).getBytes(10024*10024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bb = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        ImageView img = new ImageView(getApplicationContext());

                       // img.setLayoutParams(new ViewGroup.LayoutParams(500,500));
                        img.setImageBitmap(bb);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                        img.setLayoutParams(params);
                       img.setScaleType(ImageView.ScaleType.FIT_XY);
                        img.setPadding(5, 10, 5, 10);

                        lin.addView(img);

                        Log.i("SUCCESS:","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("FAILURE:","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

                    }
                });}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

//        for (String st:arr){
//            Log.i("HELLO",st);
//            FirebaseStorage.getInstance().getReference().child("Images").child(str).child(st).getBytes(10024*10024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bb = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                    ImageView img = new ImageView(getApplicationContext());
//                    img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                    img.setScaleType(ImageView.ScaleType.FIT_XY);
//
//                    img.setPadding(5, 10, 5, 10);
//                    img.setImageBitmap(bb);
//                    lin.addView(img);
//                    Log.i("SUCCESS:","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.i("FAILURE:","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//
//                }
//            });

        }

    }


package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Photo extends AppCompatActivity {

    Uri im;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn=getMenuInflater();
        mn.inflate(R.menu.second,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.home:
                Intent it=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(it);
                return true;
            default:
                return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                View v=findViewById(R.id.button);
                browse(v);
            }
        }
    }

    public void uploadphoto(View view){

        final String ima= UUID.randomUUID().toString()+".jpg";
        try{Bitmap bp=MediaStore.Images.Media.getBitmap(this.getContentResolver(),im);

        ByteArrayOutputStream ba=new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG,100,ba);
        byte[] baa=ba.toByteArray();
        UploadTask up=FirebaseStorage.getInstance().getReference().child("Images").child(MainActivity.mAuth.getCurrentUser().getUid()).child(ima).putBytes(baa);
            final DatabaseReference ree= FirebaseDatabase.getInstance().getReference().child("Images").child(MainActivity.mAuth.getCurrentUser().getUid());
        final String key=ree.push().getKey();
        ree.child(key).setValue(ima);
        up.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Photo.this, "Unable to Upload Photo due to :"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.i("ERROR:",e.getLocalizedMessage()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ree.child(key).setValue(ima);

                Toast.makeText(Photo.this, "Successfully uploaded.", Toast.LENGTH_SHORT).show();
                Log.i("SUCCESS","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

            }
        });}catch(Exception e){
            Log.i("ERROR:",e.getLocalizedMessage()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
            Toast.makeText(this,"Select Image to be uploaded", Toast.LENGTH_SHORT).show();

        }

    }


    public void browse(View view){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }catch(Exception e){
            Log.i("ERROR:",e.getLocalizedMessage()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
        }


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK){
            if(data!=null){

                try {
                    im=data.getData();
                    String ima= UUID.randomUUID().toString()+".jpg";
                    Bitmap bp=MediaStore.Images.Media.getBitmap(this.getContentResolver(),im);

                    ByteArrayOutputStream ba=new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.PNG,100,ba);
                    byte[] baa=ba.toByteArray();
                    ImageView imageView=findViewById(R.id.imageView);
                    imageView.setImageBitmap(bp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));

        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }

    }
}

package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    FirebaseUser user;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn=getMenuInflater();
        mn.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(user.isEmailVerified()) {
            Log.i("EMAILVERIFIED","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");

            try {
                menu.removeItem(R.id.verify);
            } catch (Exception e) {
                Log.i("ERROR:", e.getLocalizedMessage() + "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            }
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.out:
                try{MainActivity.mAuth.signOut();
                    Intent in=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                    Log.i("SUCCESS: ","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
                }
                catch(Exception e){
                    Log.i("FAILURE: ","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");

                }
                return true;
            case R.id.verify:
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Email sent", "Email sentssssssssssssssssssssssssssssssssssssss");
                                    Toast.makeText(Main2Activity.this, "Click on the link sent to your email id to complete verification procedure.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Main2Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("ERROR:",task.getException().toString());
                                }
                            }
                        });
                return true;
            case R.id.upload:
                Intent in=new Intent(getApplicationContext(),Photo.class);
                startActivity(in);
                return true;
            case R.id.chat:
                Intent inn=new Intent(getApplicationContext(),Users.class);
                startActivity(inn);
                return true;
            case R.id.feed:

                Intent kkk=new Intent(getApplicationContext(),Feed.class);
                kkk.putExtra("UID",MainActivity.mAuth.getCurrentUser().getUid());
                startActivity(kkk);
                return true;
            case R.id.ofeed:
                Intent ola=new Intent(getApplicationContext(),Others.class);
                startActivity(ola);
                return true;

            default:
                return false;

        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));

        }
        user=MainActivity.mAuth.getCurrentUser();
        TextView tv=findViewById(R.id.textView);
        tv.setText(user.getEmail());




        }

}

package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity  implements View.OnKeyListener{

    EditText email;
    EditText pass;
    EditText username;
    ActionCodeSettings actionCodeSettings;
    public static FirebaseAuth mAuth;
    public static String userr;
    public void login(){
        userr=username.getText().toString();
        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show();
    }

    public void go(View view){

        String a=email.getText().toString();
        String b=pass.getText().toString();


        if(a.length()>0 && b.length()>0 && username.getText().toString().length()>0)
        {

            //Log.i("FAULT IN IFF","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

            mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGGED IN: ", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "signInWithEmail:success", Toast.LENGTH_SHORT).show();


                            login();



                            //updateUI(user);
                        } else {

                            mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Log.d("ACCOUNT CREATED", "createUserWithEmail:success");
                                                Toast.makeText(MainActivity.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                                                //FirebaseUser user = mAuth.getCurrentUser();
                                                //updateUI(user);
                                                user nn=new user(email.getText().toString(),username.getText().toString());
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(task.getResult().getUser().getUid()).setValue(nn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.i("USER DATABASE UPDATED","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.i("FAILURE:",e.getLocalizedMessage()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                                //FirebaseDatabase.getInstance().getReference().child("Users").child(task.getResult().getUser().getUid()).child("Password").setValue(pass.getText().toString());

                                                login();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("FAILURE:", "createUserWithEmail:failuressssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss", task.getException());
                                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }
                                    });
                        }

                        // ...
                    }
                });}
            else{
            Toast.makeText(this,"FILL IN THE REQUIRED FIELDS", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.cardview_dark_background));

        }
        email=(EditText)findViewById(R.id.Email);
        pass=(EditText)findViewById(R.id.Password);
        username=(EditText)findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        pass.setOnKeyListener(this);
        //Toast.makeText(this, "HELLOTHERE", Toast.LENGTH_SHORT).show();
        if(mAuth.getCurrentUser()!=null)
        {
            FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())){
                        user nn=dataSnapshot.getValue(user.class);
                        userr=nn.getUsername();
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);

        }


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            go(v);

        }
        return false;
    }
}

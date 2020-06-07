package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users extends AppCompatActivity {
    final HashMap<String,String> req= new HashMap<String,String>();
    final ArrayList<String> arr=new ArrayList<String>();
    HashMap<String,Boolean> chk=new HashMap<String,Boolean>();

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inf=getMenuInflater();

        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
        int pos=info.position;

        if(chk.get(req.get(arr.get(pos)))==null)
            inf.inflate(R.menu.permission,menu);
        else if(chk.get(req.get(arr.get(pos)))==false)
            inf.inflate(R.menu.permission,menu);
        else
            inf.inflate(R.menu.permsec, menu);






    }


    //    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        int pos=info.position;
//
//        if(chk.get(req.get(arr.get(pos))+MainActivity.mAuth.getCurrentUser().getUid()))
//            menu.removeItem(R.id.allow);
//        else
//            menu.removeItem(R.id.notallow);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch(item.getItemId()){
            case R.id.allow:
                Log.i("HEEEHEE","HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"+req.get(arr.get(index)));

                FirebaseDatabase.getInstance().getReference().child("FeedPermissions").child(MainActivity.mAuth.getCurrentUser().getUid()+"-"+MainActivity.userr).child(req.get(arr.get(index))).setValue(arr.get(index)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Users.this,"Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Users.this, "Permission couldn't be Granted. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            case R.id.notallow:
                Log.i("HEEEHEE","HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"+req.get(arr.get(index)));
                FirebaseDatabase.getInstance().getReference().child("FeedPermissions").child(MainActivity.mAuth.getCurrentUser().getUid()+"-"+MainActivity.userr).child(req.get(arr.get(index))).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Users.this,"Permission Revoked", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Users.this, "Permission couldn't be Revoked. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;

                default:
                    return false;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));

        }

        final ListView lv=findViewById(R.id.lv);

        final ArrayAdapter<String> adp=new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arr){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.GRAY);

                return view;
            }
        };
        lv.setAdapter(adp);
        FirebaseDatabase.getInstance().getReference().child("FeedPermissions").child(MainActivity.mAuth.getCurrentUser().getUid()+"-"+MainActivity.userr).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {chk.put(dataSnapshot.getKey(),true); }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { chk.put(dataSnapshot.getKey(),false); }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                user nn=dataSnapshot.getValue(user.class);
                if(!dataSnapshot.getKey().equals(MainActivity.mAuth.getCurrentUser().getUid())) {
                    arr.add(nn.getUsername());
                    req.put(nn.getUsername(), dataSnapshot.getKey());
                    adp.notifyDataSetChanged();
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ne=new Intent(getApplicationContext(),Chat.class);
                ne.putExtra("UID",req.get(arr.get(position)));
                startActivity(ne);
            }
        });
      //  Toast.makeText(this, Integer.toString(lv.getId()), Toast.LENGTH_SHORT).show();

        registerForContextMenu(lv);
        //arr.add("KHOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

/*        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null) {
                    if (objects.size() > 0) {
                        for (ParseUser usr : objects) {
                            arr.add(usr.getUsername());

                        }
                        lv.setAdapter(adp);


                    }
                }

                else{

                    e.printStackTrace();
                    Log.i("ERROR",e.getLocalizedMessage()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

                }


            }
        });*/


    }
}

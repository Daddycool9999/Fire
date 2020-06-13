package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Others extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.cardview_dark_background));

        }
        final ListView lv=findViewById(R.id.lv);
        final HashMap<String,String> req= new HashMap<String,String>();
        final ArrayList<String> arr=new ArrayList<String>();

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

        FirebaseDatabase.getInstance().getReference().child("FeedPermissions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(!dataSnapshot.getKey().equals(MainActivity.mAuth.getCurrentUser().getUid()+"-"+MainActivity.userr)) {



                    String [] tokens=dataSnapshot.getKey().split("-");
                    Log.i("HhHH",tokens[0]+tokens[1]+"JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
                    final String ss=tokens[1];
                    final String kk=tokens[0];

                    FirebaseDatabase.getInstance().getReference().child("FeedPermissions").child(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.getKey().equals(MainActivity.mAuth.getCurrentUser().getUid()))
                            {
                                arr.add(ss);
                                req.put(ss,kk);
                                adp.notifyDataSetChanged();

                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getKey().equals(MainActivity.mAuth.getCurrentUser().getUid())){
                            arr.remove(ss);
                            req.remove(ss);
                            adp.notifyDataSetChanged();

                        }

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
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
                Intent ne=new Intent(getApplicationContext(),Feed.class);
                ne.putExtra("UID",req.get(arr.get(position)));
                startActivity(ne);
            }
        });
        //  Toast.makeText(this, Integer.toString(lv.getId()), Toast.LENGTH_SHORT).show();

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
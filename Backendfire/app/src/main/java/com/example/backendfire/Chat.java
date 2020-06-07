package com.example.backendfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    EditText messag;
    String str;
    String cur;
    String tostore;
    //ArrayList<String> arr;
    LinearLayout layout;
    ScrollView scrollView;


    public void send(View view){
        String mes=messag.getText().toString();
        messag.setText("");
        if(mes!=null && !mes.equals("")){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Messages").child(tostore);
        message mm=new message(mes,cur);
        String key=ref.push().getKey();
        ref.child(key).setValue(mm).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chat.this, "FAILURE IN SENDING MESSAGE", Toast.LENGTH_SHORT).show();
            }
        }); }
        else{
            Toast.makeText(this, "U are supposed to type something", Toast.LENGTH_SHORT).show();
        }
        //arr.add("--> "+mes);
    }


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

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);

        textView.setText(message);
        textView.setTextSize(25);


        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp2.weight=1;

        if(type == 2) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.out);
        }
        textView.setPadding(15,15,30,15);
        textView.setMovementMethod(new ScrollingMovementMethod());
//        textView.setSingleLine(false);
//        textView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        textView.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        textView.setLayoutParams(lp2);
        //Log.i("HERE:",Integer.toString(textView.getInputType()));

        layout.addView(textView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.common_google_signin_btn_text_light_pressed));

        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();
        str= intent.getStringExtra("UID");
        messag=findViewById(R.id.message);
        cur=MainActivity.mAuth.getCurrentUser().getUid();
        if(cur.compareTo(str)>0)
            tostore=cur+"-"+str;
        else
            tostore=str+"-"+cur;

        layout=findViewById(R.id.layout);
        scrollView=findViewById(R.id.scrollView);

        /*final ListView lv=findViewById(R.id.lv);
        arr=new ArrayList<String>();
        final ArrayAdapter<String> adp=new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arr){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);


                textView.setTextColor(Color.GRAY);

                return view;
            }
        };
       lv.setAdapter(adp);*/
        FirebaseDatabase.getInstance().getReference().child("Messages").child(tostore).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                message hel=dataSnapshot.getValue(message.class);
                    if(hel.getSender().equals(cur))
                        //arr.add("--> "+hel.getMessageText());
                        addMessageBox(hel.getMessageText(),1);
                    else
                       // arr.add(hel.getMessageText());
                    addMessageBox(hel.getMessageText(),2);

                    //adp.notifyDataSetChanged();


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



    }
}

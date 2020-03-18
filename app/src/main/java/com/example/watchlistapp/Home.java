package com.example.watchlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.watchlistapp.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    private TextView editTextWelcome;
    private TextView editTextLogout;
    private Button buttonTest;
    private Button buttonWatchList;
    private DatabaseReference databaseUsers;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle b = getIntent().getExtras();
        final String email;
        if(b != null)
            email = b.getString("email");
        else {
            email="";
            startActivity(new Intent(Home.this, MainActivity.class));
        }
        editTextWelcome = (TextView) findViewById(R.id.homeWelcome);
        editTextLogout = (TextView) findViewById(R.id.homeLogOut);
        buttonTest= (Button) findViewById(R.id.homeButton1);
        buttonWatchList = (Button) findViewById(R.id.homeButton2);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if(user.getUserEmail().equals(email)) {
                        id=userSnapshot.getKey();
                        editTextWelcome.setText("Hi " + user.getUserName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        editTextLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,MainActivity.class));
            }
        });
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Questions.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        buttonWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, WatchList.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
}

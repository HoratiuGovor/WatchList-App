package com.example.watchlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.watchlistapp.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class WatchList extends AppCompatActivity {
    private Button home;
    private ListView movieList;
    private ImageView logo;
    private String email;
    private String id;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseMovies;
    private String movieIds;
    private ArrayAdapter<String>adapter;
    private ArrayList<String> arrayListMovies = new ArrayList<>();
    private ArrayList<String> arrayListMoviesKeys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        logo= (ImageView) findViewById(R.id.watchListImageViewLogo);
        movieList= (ListView) findViewById(R.id.watchListMovieList);
        home = (Button) findViewById(R.id.watchListButtonHome);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseMovies = FirebaseDatabase.getInstance().getReference("movies");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayListMovies);
        movieList.setAdapter(adapter);
        getBundle();
        getMovies();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchList.this, Home.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    System.out.println(arrayListMovies.get(i));
                    System.out.println(arrayListMoviesKeys.get(i));
                    Intent intent = new Intent(WatchList.this, MovieDescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("id", id);
                    bundle.putString("movieId", arrayListMoviesKeys.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }catch (Exception e){}
            }
        });
    }
    private void getBundle(){
        Bundle b = getIntent().getExtras();
        if(b != null){
            email = b.getString("email");
            id = b.getString("id");}
        else {
            id="";
            startActivity(new Intent(WatchList.this, MainActivity.class));
        }
    }
    private void getMovies() {
        boolean loading = true;
        while (loading) {
            try {
                databaseUsers.child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        movieIds = dataSnapshot.child("userTitles").getValue(String.class);
                        arrayListMovies.clear();
                        if(movieIds.length()==0){
                            home.setText("No movies here,go home");
                        }
                        else {
                            final String[] tokens = movieIds.split(",");
                            for (String movieId :
                                    tokens) {
                                databaseMovies.child(movieId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String movie_title = dataSnapshot.child("movie_title").getValue(String.class);
                                        arrayListMovies.add(movie_title);
                                        arrayListMoviesKeys.add(movieId);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        //arrayListMovies.addAll(Arrays.asList(tokens));
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                loading=false;
            }catch (Exception e){}
        }
    }
}

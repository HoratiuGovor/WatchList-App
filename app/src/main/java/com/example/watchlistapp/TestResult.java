package com.example.watchlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watchlistapp.Models.Movie;
import com.example.watchlistapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Random;

public class TestResult extends AppCompatActivity {
    private Integer randomNo;
    private String firstOption;
    private String secondOption;
    private Integer genreScoreDifference;
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieGenre;
    private Button addWatchlist;
    private Button retry;
    private Button home;
    private DatabaseReference databaseMovies;
    private DatabaseReference databaseUsers;
    private String email;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        moviePoster= (ImageView) findViewById(R.id.resultImageViewResult);
        movieTitle= (TextView) findViewById(R.id.resultTextViewMovieTitle);
        movieGenre= (TextView) findViewById(R.id.resultTextViewMovieGenre);
        addWatchlist= (Button) findViewById(R.id.resultButtonAddWatchlist);
        retry= (Button) findViewById(R.id.resultButtonAnotherResult);
        home= (Button) findViewById(R.id.resultButtonHome);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseMovies = FirebaseDatabase.getInstance().getReference("movies");

        Bundle b = getIntent().getExtras();
        try {
            firstOption = b.getString("firstOption");
            secondOption = b.getString("secondOption");
            genreScoreDifference = b.getInt("genreScoreDifference");
            email = b.getString("email");
            id = b.getString("id");
        }catch (Exception e){
            firstOption="";
            secondOption="";
            genreScoreDifference=0;
            email = "";
            id = "";
        }
        addWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWatchList();
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    findMovie();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestResult.this, Home.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        findMovie();
    }

    private void findMovie() {
        addWatchlist.setText("Add to Watch List");
        movieTitle.setText("Loading...");
        randomNo = new Random().nextInt(5041);
        randomNo++;
        System.out.println("random"+randomNo);
        databaseMovies.child(randomNo.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Movie movie = makeMovieObject(dataSnapshot);
                    try {
                        if (genreScoreDifference > 2 && movie.getGenres().contains(firstOption)){
                            movieTitle.setText(movie.getMovie_title());
                            String genre = "Recomanded genre: "+firstOption;
                            movieGenre.setText(genre);
                            getPosterAPI(movie.getMovie_title());

                        }
                        else if (movie.getGenres().contains(firstOption) && movie.getGenres().contains(secondOption)){
                            movieTitle.setText(movie.getMovie_title());
                            String genre = "Recomanded genre: "+firstOption+", "+secondOption;
                            movieGenre.setText(genre);
                            getPosterAPI(movie.getMovie_title());
                            }
                        else
                            findMovie();
                    }catch (Exception e){findMovie();}
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addToWatchList(){
        final User user = new User();
        databaseUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    user.setUserTitles( dataSnapshot.child("userTitles").getValue(String.class));
                    /*String id2 = databaseUsers.push().getKey();*/
                    //databaseUsers.child(id).child("userTitles").child(id2).setValue(user.getUserTitles()+String.valueOf(randomNo));
                    if(!user.getUserTitles().contains(String.valueOf(randomNo))) {
                        if (!user.getUserTitles().equals(""))
                            databaseUsers.child(id).child("userTitles").setValue(user.getUserTitles() + "," + String.valueOf(randomNo));
                        else
                            databaseUsers.child(id).child("userTitles").setValue(String.valueOf(randomNo));
                        addWatchlist.setText("Added to Watch List");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //databaseUsers.child(id).child("userTitles").setValue(user.getUserTitles()+String.valueOf(randomNo));
    }
    Movie makeMovieObject(DataSnapshot dataSnapshot){
        String director_name,duration,genres,imdb_score,movie_imdb_link,movie_title,poster,title_year;
        Integer durationInt,title_yearInt;
        Double imdb_scoreLong;
        director_name = dataSnapshot.child("director_name").getValue(String.class);
        genres = dataSnapshot.child("genres").getValue(String.class);
        movie_imdb_link = dataSnapshot.child("movie_imdb_link").getValue(String.class);
        movie_title = dataSnapshot.child("movie_title").getValue(String.class);
        poster = dataSnapshot.child("poster").getValue(String.class);
        try {
            duration = dataSnapshot.child("duration").getValue(String.class);
        }catch(Exception e){
            durationInt = dataSnapshot.child("duration").getValue(Integer.class);
            duration = Integer.toString(durationInt);
        }
        try {
            imdb_score = dataSnapshot.child("imdb_score").getValue(String.class);
        }catch(Exception e){
            imdb_scoreLong = dataSnapshot.child("imdb_score").getValue(Double.class);
            imdb_score = Double.toString(imdb_scoreLong);
        }
        try {
            title_year = dataSnapshot.child("title_year").getValue(String.class);
        }catch(Exception e){
            title_yearInt = dataSnapshot.child("title_year").getValue(Integer.class);
            title_year = Integer.toString(title_yearInt);
        }
        return new Movie(director_name,duration,genres,movie_title,movie_imdb_link,title_year,imdb_score,poster);
    }
    public void getPosterAPI(String movieTitleStr){
        movieTitleStr = movieTitleStr.replaceAll("\\s+$", "");
        movieTitleStr = movieTitleStr.replaceAll("\\s+","%20");
        String url = "http://www.omdbapi.com/?t="+movieTitleStr+"&apikey=d1f5870";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject movieJson = new JSONObject(response);
                    String posterUrl = movieJson.getString("Poster");
                    if(!posterUrl.equals("N/A"))
                        Picasso.get().load(posterUrl).into(moviePoster);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}

package com.example.watchlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.RegEx;

public class MovieDescription extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieGenre;
    private Button imdbLink;
    private Button delete;
    private Button back;
    private String movieId;
    private String email;
    private String id;
    private String movieTitleStr="";
    private String movieLinkStr;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        moviePoster= (ImageView) findViewById(R.id.movieDescImageViewPoster);
        movieTitle= (TextView) findViewById(R.id.movieDescTextViewMovieTitle);
        movieGenre= (TextView) findViewById(R.id.movieDescTextViewMovieGenre);
        imdbLink= (Button) findViewById(R.id.movieDescButtonImdb);
        delete= (Button) findViewById(R.id.movieDescButtonDelete);
        back= (Button) findViewById(R.id.movieDescButtonBack);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseMovies = FirebaseDatabase.getInstance().getReference("movies");
        getBundle();
        showMovie();
        imdbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!movieLinkStr.equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(movieLinkStr));
                    startActivity(intent);}
                    else {
                        Toast.makeText(MovieDescription.this, "No link in the database",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMovie();
                Intent intent = new Intent(MovieDescription.this, WatchList.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDescription.this, WatchList.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void getBundle() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            email = b.getString("email");
            id = b.getString("id");
            movieId = b.getString("movieId");
        }
        else {
            id="";
            startActivity(new Intent(MovieDescription.this, MainActivity.class));
        }
    }
    private void showMovie() {
        boolean loading = true;
        while (loading) {
            try {
                databaseMovies.child(movieId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String movie_title = dataSnapshot.child("movie_title").getValue(String.class);
                        String genres = dataSnapshot.child("genres").getValue(String.class);
                        movieTitle.setText(movie_title);
                        movieGenre.setText(genres);
                        movieTitleStr=movie_title;
                        getPosterAPI();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                loading = false;
            } catch (Exception e) {

            }
        }
    }
    private void deleteMovie() {
        boolean loading = true;
        while (loading) {
            try {
                databaseUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String movieIds = dataSnapshot.child("userTitles").getValue(String.class);
                        final String[] tokens = movieIds.split(",");
                        String result="";
                        for (String movieIdDb :
                                tokens) {
                            if(!movieIdDb.equals(movieId)){
                                result=result+movieIdDb+",";
                            }
                        }
                        if(tokens.length>1)
                            result = result.substring(0, result.length() - 1);
                        databaseUsers.child(id).child("userTitles").setValue(String.valueOf(result));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                loading = false;
            } catch (Exception e) {

            }
        }
    }
    public void getPosterAPI(){
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
                    movieLinkStr ="https://www.imdb.com/title/"+movieJson.getString("imdbID");
                    if(!posterUrl.equals("N/A"))
                        Picasso.get().load(posterUrl).into(moviePoster);
                } catch (Exception e) {
                    movieLinkStr="";
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

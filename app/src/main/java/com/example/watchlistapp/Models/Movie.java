package com.example.watchlistapp.Models;

public class Movie {
    private String director_name;
    private String duration;
    private String genres;
    private String movie_title;
    private String movie_imdb_link;
    private String title_year;
    private String imdb_score;
    private String poster;

    public Movie(){}
    public Movie(String director_name, String duration, String genres, String movie_title, String movie_imdb_link, String title_year, String imdb_score, String poster) {
        this.director_name = director_name;
        this.duration = duration;
        this.genres = genres;
        this.movie_title = movie_title;
        this.movie_imdb_link = movie_imdb_link;
        this.title_year = title_year;
        this.imdb_score = imdb_score;
        this.poster=poster;
    }

    public String getDirector_name() {
        return director_name;
    }

    public void setDirector_name(String director_name) {
        this.director_name = director_name;
    }


    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_imdb_link() {
        return movie_imdb_link;
    }

    public void setMovie_imdb_link(String movie_imdb_link) {
        this.movie_imdb_link = movie_imdb_link;
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle_year() {
        return title_year;
    }

    public void setTitle_year(String title_year) {
        this.title_year = title_year;
    }

    public String getImdb_score() {
        return imdb_score;
    }

    public void setImdb_score(String imdb_score) {
        this.imdb_score = imdb_score;
    }
}

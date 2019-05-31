package com.example.usuario.batmanapp.Utils;

public class MovieCard {

    //Clase de las CardViews de la actividad principal. Posee su constructor, getters y setters.

    private String movie_Poster;
    private String movie_Title;
    private String movie_Year;

    public MovieCard(String movie_Poster, String movie_Title, String movie_Year) {
        this.movie_Poster = movie_Poster;
        this.movie_Title = movie_Title;
        this.movie_Year = movie_Year;
    }

    public String getMovie_Poster() {
        return movie_Poster;
    }

    public void setMovie_Poster(String movie_Poster) {
        this.movie_Poster = movie_Poster;
    }

    public String getMovie_Title() {
        return movie_Title;
    }

    public void setMovie_Title(String movie_Title) {
        this.movie_Title = movie_Title;
    }

    public String getMovie_Year() {
        return movie_Year;
    }

    public void setMovie_Year(String movie_Year) {
        this.movie_Year = movie_Year;
    }

}

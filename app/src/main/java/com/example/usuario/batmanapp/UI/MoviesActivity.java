package com.example.usuario.batmanapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.usuario.batmanapp.Controller.MovieAdapter;
import com.example.usuario.batmanapp.Utils.MovieCard;
import com.example.usuario.batmanapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {

    //region Variables del Recycler view
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieCard> movieList;
    //endregion

    //region Datos propios de peliculas de Batman

    //Listado de las peliculas de Batman sin contar las animadas.
    String[] batman_movie_name = new String[]{
            "Batman",
            "Batman returns",
            "Batman forever",
            "Batman and robin",
            "Batman Begins",
            "The Dark Knight",
            "The Dark Knight Rises",
            "Batman v Superman: Dawn of Justice"
    };

    //Variables para manejar los datos que se le envian al sitio que contiene los json.
    int batman_movie_number = batman_movie_name.length;
    String batman_movie_url = "";
    String batman_movie_name_fixed = "";

    //endregion

    //region Obtener datos principales(Titulo, año y url del poster) de las peliculas con json.

    public class fetchMovieData extends AsyncTask<Void,Void,Void> {

        private ProgressDialog progress;

        //Variables para obtencion y manejo de json
        String fetch_data = "";
        String movie_title = "";
        String movie_year = "";
        String movie_poster = "";
        private String url;

        //region Constructor/Get/Set
        public fetchMovieData(String url){
            this.url = url;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        //endregion

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Obtencion de data del sitio:
                URL url = new URL(getUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    fetch_data = fetch_data + line;
                }

                //Separar los datos del json obtenido y asignarlos a variables.
                if(fetch_data != null){
                    JSONObject reader = new JSONObject(fetch_data);
                    movie_title = (String) reader.get("Title");
                    movie_year = (String) reader.get("Year");
                    movie_poster = (String) reader.get("Poster");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MoviesActivity.this);
            progress.setMax(100);
            progress.setMessage("Cargando...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            movieList.add(new MovieCard(movie_poster,movie_title,movie_year));
            movieAdapter = new MovieAdapter(MoviesActivity.this,movieList);
            recyclerView.setAdapter(movieAdapter);
            movieAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Adaptador para que al hacer click en la CardView abra la actividad que contiene todos los datos requeridos de la pelicula.
                    String clicked_title = movieList.get(recyclerView.getChildAdapterPosition(view)).getMovie_Title();
                    Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    intent.putExtra( "movie_title", clicked_title);
                    startActivity(intent);
                }
            });
            progress.dismiss();
        }
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //region Atributos del recycler view
        recyclerView = findViewById(R.id.recyclerView_MainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        //endregion

        //Para cada pelicula de Batman listada, se realiza un request a el sitio, enviando el titulo de la pelicula.
        for(int i=0; i<batman_movie_number; i++){
            batman_movie_name_fixed = batman_movie_name[i].replace(' ', '+');
            batman_movie_url = "http://www.omdbapi.com/?t=" + batman_movie_name_fixed + "&apikey=a58cdb03";
            fetchMovieData process = new fetchMovieData(batman_movie_url);
            process.execute();
        }

    }

}

package com.example.usuario.batmanapp.UI;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.usuario.batmanapp.R;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {

    //region Variables de la informacion de la pelicula

    //Para el manejo del json
    public String batman_movie_name_fixed;
    public String batman_movie_url;

    //Para llenar la informacion en el XML
    public String movie_title = "";
    public String movie_year = "";
    public String movie_released = "";
    public String movie_genre = "";
    public String movie_poster = "";
    public String movie_director = "";
    public String movie_actors = "";
    public String movie_plot = "";
    public String movie_imdbRating = "";

    //endregion

    public class fetchMovieData extends AsyncTask<Void,Void,Void> {

        private ProgressDialog progress;

        //Variables para obtencion y manejo de json
        String fetch_data = "";
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
                    movie_year = (String) reader.get("Year");
                    movie_released = (String) reader.get("Released");
                    movie_genre = (String) reader.get("Genre");
                    movie_poster = (String) reader.get("Poster");
                    movie_director = (String) reader.get("Director");
                    movie_actors = (String) reader.get("Actors");
                    movie_plot = (String) reader.get("Plot");
                    movie_imdbRating = (String) reader.get("imdbRating");
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
            progress = new ProgressDialog(MovieDetailsActivity.this);
            progress.setMax(100);
            progress.setMessage("Cargando...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //region Asignar datos obtenidos al XML

            TextView movieDetails_tv_Title = findViewById(R.id.movieDetails_tv_title);
            TextView movieDetails_tv_Year = findViewById(R.id.movieDetails_tv_year);
            TextView movieDetails_tv_release = findViewById(R.id.movieDetails_tv_release);
            TextView movieDetails_tv_genre = findViewById(R.id.movieDetails_tv_genre);
            TextView movieDetails_tv_director = findViewById(R.id.movieDetails_tv_director);
            TextView movieDetails_tv_actors = findViewById(R.id.movieDetails_tv_actors);
            TextView movieDetails_tv_plot = findViewById(R.id.movieDetails_tv_plot);
            TextView movieDetails_tv_imdb = findViewById(R.id.movieDetails_tv_imdb);
            ImageView movieDetails_iv_poster = findViewById(R.id.movieDetails_iv_poster);

            movieDetails_tv_Title.setText(movie_title);
            movieDetails_tv_Year.setText(movie_year);
            movieDetails_tv_release.setText(movie_released);
            movieDetails_tv_genre.setText(movie_genre);
            movieDetails_tv_director.setText(movie_director);
            movieDetails_tv_actors.setText(movie_actors);
            movieDetails_tv_plot.setText(movie_plot);
            movieDetails_tv_imdb.setText(movie_imdbRating);
            Picasso.with(getApplicationContext()).load(movie_poster).fit().centerInside().into(movieDetails_iv_poster);

            //endregion

            progress.dismiss();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Se obtiene el titulo de la pelicula enviada por la CardView a la cual se le hizo click.
        movie_title = getIntent().getStringExtra("movie_title");
        batman_movie_name_fixed = movie_title.replace(' ', '+');
        //Envio del request a el sitio:
        batman_movie_url = "http://www.omdbapi.com/?t=" + batman_movie_name_fixed + "&apikey=a58cdb03";
        fetchMovieData process = new fetchMovieData(batman_movie_url);
        process.execute();

    }

}

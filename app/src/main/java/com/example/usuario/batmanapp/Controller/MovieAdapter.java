package com.example.usuario.batmanapp.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.usuario.batmanapp.Utils.MovieCard;
import com.example.usuario.batmanapp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements View.OnClickListener{

    //Adaptador que recibe los datos para rellenar las CardViews de las peliculas en el listado de la actividad principal.
    //Tambien a cada CardView le agrega un listener para cuando se le haga click a esta.

    private Context mcontext;
    private ArrayList <MovieCard> movieList;
    private View.OnClickListener listener;

    public MovieAdapter(Context context, ArrayList<MovieCard> list){
        mcontext = context;
        movieList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.movie_card, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieCard currentCard = movieList.get(position);
        String title = currentCard.getMovie_Title();
        String year = currentCard.getMovie_Year();
        String poster = currentCard.getMovie_Poster();

        holder.textView_title.setText(title);
        holder.textView_year.setText("Año: " + year);
        Picasso.with(mcontext).load(poster).fit().centerInside().into(holder.imageView_poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_poster;
        public TextView textView_title;
        public TextView textView_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_poster = itemView.findViewById(R.id.cardView_iv_poster);
            textView_title = itemView.findViewById(R.id.cardView_tv_title);
            textView_year = itemView.findViewById(R.id.cardView_tv_year);
        }
    }

}

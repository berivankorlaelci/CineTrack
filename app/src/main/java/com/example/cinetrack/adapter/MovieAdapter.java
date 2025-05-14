package com.example.cinetrack.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinetrack.R;
import com.example.cinetrack.model.Movie;
import com.example.cinetrack.ui.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;

    // Constructor
    public MovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    // ViewHolder: Her satırdaki görünümleri tutar
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textTitle, textYear;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textYear = itemView.findViewById(R.id.textYear);
        }
    }

    // item_movie.xml dosyasını şişirir (inflate eder)
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    // Görünümlere verileri bağlar
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.textTitle.setText(movie.getTitle());
        holder.textYear.setText(movie.getYear());

        // Poster URL boş değilse yükle
        if (movie.getPoster() != null && !movie.getPoster().equals("N/A")) {
            Picasso.get()
                    .load(movie.getPoster())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imagePoster);
        } else {
            holder.imagePoster.setImageResource(R.drawable.ic_launcher_background); // varsayılan görsel
        }

        //Film tıklanınca yeni ekrana aktaracağımız verilerin tanımlanması
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("year", movie.getYear());
            intent.putExtra("poster", movie.getPoster());
            intent.putExtra("imdbID", movie.getImdbID());
            intent.putExtra("type", movie.getType());
            context.startActivity(intent);
        });
    }

    // Liste uzunluğu
    @Override
    public int getItemCount() {
        return movieList.size();
    }
}

package com.example.cinetrack.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinetrack.R;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView textTitle, textYear, textType, textImdbID,textRating,textPlot;
    private ImageView imagePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        textTitle = findViewById(R.id.textDetailTitle);
        textYear = findViewById(R.id.textDetailYear);
        textType = findViewById(R.id.textDetailType);
        textImdbID = findViewById(R.id.textDetailImdbID);
        imagePoster = findViewById(R.id.imageDetailPoster);
        textRating = findViewById(R.id.textDetailRating);
        textPlot = findViewById(R.id.textDetailPlot);


        // Intent'ten verileri al
        String title = getIntent().getStringExtra("title");
        String year = getIntent().getStringExtra("year");
        String type = getIntent().getStringExtra("type");
        String imdbID = getIntent().getStringExtra("imdbID");
        String poster = getIntent().getStringExtra("poster");

        String apiKey = "40a2a2aa";
        String url = "https://www.omdbapi.com/?apikey=" + apiKey + "&i=" + imdbID;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest detailRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String plot = response.optString("Plot");
                        String imdbRating = response.optString("imdbRating");

                        textPlot.setText("📝 " + plot);
                        textRating.setText("⭐ IMDb Puanı: " + imdbRating);
                    } catch (Exception e) {
                        e.printStackTrace();
                        textPlot.setText("Açıklama alınamadı.");
                        textRating.setText("Puan yok.");
                    }
                },
                error -> {
                    error.printStackTrace();
                    textPlot.setText("Detay yüklenemedi.");
                    textRating.setText("Puan yok.");
                });

        queue.add(detailRequest);


        // Verileri arayüze aktar
        textTitle.setText("🎬 " + title);
        textImdbID.setText("🔎 IMDb ID: " + imdbID);
        textYear.setText("📅 " + year);
        textType.setText("🎞️ " + type);


        if (poster != null && !poster.equals("N/A")) {
            Picasso.get().load(poster).into(imagePoster);
        } else {
            imagePoster.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}

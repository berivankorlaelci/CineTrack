package com.example.cinetrack.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinetrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView textTitle, textYear, textType, textImdbID, textRating, textPlot;
    private ImageView imagePoster;
    private Button buttonAddFavorite, buttonBack;

    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // View'ları tanımla
        textTitle = findViewById(R.id.textDetailTitle);
        textYear = findViewById(R.id.textDetailYear);
        textType = findViewById(R.id.textDetailType);
        textImdbID = findViewById(R.id.textDetailImdbID);
        textRating = findViewById(R.id.textDetailRating);
        textPlot = findViewById(R.id.textDetailPlot);
        imagePoster = findViewById(R.id.imageDetailPoster);
        buttonAddFavorite = findViewById(R.id.buttonAddFavorite);
        buttonBack = findViewById(R.id.buttonBack); // Geri butonu XML'de tanımlanmalı

        // Firebase Auth kontrolü
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        // Intent'ten gelen veriler
        String title = getIntent().getStringExtra("title");
        String year = getIntent().getStringExtra("year");
        String type = getIntent().getStringExtra("type");
        String imdbID = getIntent().getStringExtra("imdbID");
        String poster = getIntent().getStringExtra("poster");

        textTitle.setText("🎬 " + title);
        textImdbID.setText("🔎 IMDb ID: " + imdbID);
        textYear.setText("📅 " + year);
        textType.setText("🎞️ " + type);

        if (poster != null && !poster.equals("N/A")) {
            Picasso.get().load(poster).into(imagePoster);
        } else {
            imagePoster.setImageResource(R.drawable.ic_launcher_background);
        }

        // OMDb'den detay çek
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

                        // Favorilere Ekle butonu
                        buttonAddFavorite.setOnClickListener(v -> {
                            Map<String, Object> favMovie = new HashMap<>();
                            favMovie.put("title", title);
                            favMovie.put("year", year);
                            favMovie.put("poster", poster);
                            favMovie.put("imdbID", imdbID);
                            favMovie.put("type", type);
                            favMovie.put("imdbRating", imdbRating);
                            favMovie.put("plot", plot);

                            db.collection("users")
                                    .document(uid)
                                    .collection("favorites")
                                    .document(imdbID)
                                    .set(favMovie)
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(this, "Favorilere eklendi", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Favori eklenemedi!", Toast.LENGTH_SHORT).show());
                        });

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

        // Geri Dön Butonu
        buttonBack.setOnClickListener(v -> finish());
    }
}

package com.example.cinetrack.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinetrack.R;
import com.example.cinetrack.adapter.MovieAdapter;
import com.example.cinetrack.model.Movie;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "40a2a2aa";
    private static final String BASE_URL = "https://www.omdbapi.com/";

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>(); // Listeyi tutar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                searchMovies(query);
            } else {
                Toast.makeText(this, "Lütfen bir film adı girin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMovies(String query) {
        String url = BASE_URL + "?apikey=" + API_KEY + "&s=" + query;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray searchArray = response.getJSONArray("Search");
                        movieList.clear(); // önceki aramaları temizle

                        for (int i = 0; i < searchArray.length(); i++) {
                            JSONObject movieObject = searchArray.getJSONObject(i);

                            String title = movieObject.optString("Title");
                            String year = movieObject.optString("Year");
                            String poster = movieObject.optString("Poster");
                            String imdbID = movieObject.optString("imdbID");
                            String type = movieObject.optString("Type");

                            Movie movie = new Movie(title, year, imdbID, type, poster);
                            movieList.add(movie);
                        }

                        movieAdapter = new MovieAdapter(movieList, this);
                        recyclerView.setAdapter(movieAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Film bulunamadı veya JSON hatası:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace(); // Hatanın tüm detayını logcat'e yazdır
                    Log.e("OMDbAPI", "Volley Hatası: " + error.toString()); // Kısa hata açıklaması
                    Toast.makeText(this, "API bağlantı hatası: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}

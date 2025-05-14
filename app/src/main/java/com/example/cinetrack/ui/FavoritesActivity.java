package com.example.cinetrack.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinetrack.R;
import com.example.cinetrack.adapter.MovieAdapter;
import com.example.cinetrack.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerFavorites;
    private MovieAdapter adapter;
    private List<Movie> favoriteMovies = new ArrayList<>();
    private FirebaseFirestore db;
    private String uid;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerFavorites = findViewById(R.id.recyclerFavorites);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(favoriteMovies, this);
        recyclerFavorites.setAdapter(adapter);

        // Geri dön butonu
        buttonBack.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(uid).collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favoriteMovies.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = new Movie();
                        movie.setTitle(doc.getString("title"));
                        movie.setYear(doc.getString("year"));
                        movie.setImdbID(doc.getString("imdbID"));
                        movie.setType(doc.getString("type"));
                        movie.setPoster(doc.getString("poster"));
                        favoriteMovies.add(movie);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // hata yönetimi burada yapılabilir
                    e.printStackTrace();
                });
    }
}

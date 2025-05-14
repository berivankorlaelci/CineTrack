package com.example.cinetrack.model;

public class Movie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    // Constructor (JSON'dan elle veri alırken kullanılıyor)
    public Movie(String title, String year, String imdbID, String type, String poster) {
        this.Title = title;
        this.Year = year;
        this.imdbID = imdbID;
        this.Type = type;
        this.Poster = poster;
    }

    // Getter'lar (RecyclerView, Glide, TextView için)
    public String getTitle() { return Title; }
    public String getYear() { return Year; }
    public String getImdbID() { return imdbID; }
    public String getType() { return Type; }
    public String getPoster() { return Poster; }
}

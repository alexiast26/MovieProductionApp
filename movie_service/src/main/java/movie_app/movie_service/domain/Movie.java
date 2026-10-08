package movie_app.movie_service.domain;

import java.util.*;

public class Movie {
    private Integer id;
    private String name;
    private int duration;
    private int releaseYear;
    private Genre genre;
    private Category category;

    private List<String> imageUrls = new ArrayList<>();
    private List<Integer> actorIds = new ArrayList<>();
    private List<Integer> directorIds = new ArrayList<>();
    private List<Integer> screenwriterIds = new ArrayList<>();

    public Movie(){
    }

    public Movie(Integer id, String name, int duration, int releaseYear, Genre genre, Category category,
                 List<String> imageUrls,
                 List<Integer> directorIds,
                 List<Integer> screenwriterIds,
                 List<Integer> actorIds) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.category = category;
        this.imageUrls = imageUrls;
        this.directorIds = directorIds;
        this.screenwriterIds = screenwriterIds;
        this.actorIds = actorIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Integer> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<Integer> actorIds) {
        this.actorIds = actorIds;
    }

    public List<Integer> getDirectorIds() {
        return directorIds;
    }

    public void setDirectorIds(List<Integer> directorIds) {
        this.directorIds = directorIds;
    }

    public List<Integer> getScreenwriterIds() {
        return screenwriterIds;
    }

    public void setScreenwriterIds(List<Integer> screenwriterIds) {
        this.screenwriterIds = screenwriterIds;
    }
}

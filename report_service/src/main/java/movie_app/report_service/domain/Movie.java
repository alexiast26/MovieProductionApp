package movie_app.report_service.domain;
import java.util.List;

public class Movie {
    private Integer id;
    private String name;
    private int duration;
    private int releaseYear;
    private String genre;
    private String category;
    private List<Integer> actorsId;

    public Movie(Integer id, String name, int duration, int releaseYear, String genre, String category, List<Integer> actorsId) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.category = category;
        this.actorsId = actorsId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public String getCategory() {
        return category;
    }

    public List<Integer> getActorsId() {
        return actorsId;
    }
}

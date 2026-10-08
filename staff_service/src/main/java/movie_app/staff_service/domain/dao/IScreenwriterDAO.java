package movie_app.staff_service.domain.dao;

import movie_app.staff_service.domain.Screenwriter;

import java.util.*;

public interface IScreenwriterDAO {
    List<Screenwriter> getAllScreenwriters();
    Optional<Screenwriter> getScreenwriterById(Integer id);
    List<Screenwriter> getScreenwriterByName(String name);
    Screenwriter saveScreenwriter(Screenwriter screenwriter);
    void deleteScreenwriterById(Integer id);
}

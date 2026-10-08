package movie_app.staff_service.domain.dao;

import movie_app.staff_service.domain.Director;

import java.util.*;

public interface IDirectorDAO {
    List<Director> getAllDirectors();
    Optional<Director> getDirectorById(Integer id);
    List<Director> getDirectorByName(String name);
    Director saveDirector(Director director);
    void deleteDirectorById(Integer id);
}

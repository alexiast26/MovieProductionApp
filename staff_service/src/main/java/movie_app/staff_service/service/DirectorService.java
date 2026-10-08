package movie_app.staff_service.service;

import movie_app.staff_service.domain.dao.IDirectorDAO;
import movie_app.staff_service.domain.Director;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DirectorService {
    private final IDirectorDAO directorDAO;

    public DirectorService(IDirectorDAO directorDAO) {
        this.directorDAO = directorDAO;
    }

    public List<Director> getAllDirectors() {
        return directorDAO.getAllDirectors();
    }

    public Optional<Director> getDirectorById(Integer id) {
        return directorDAO.getDirectorById(id);
    }

    public List<Director> searchDirectorsByName(String name) {
        return directorDAO.getDirectorByName(name);
    }

    public Director addDirector(Director director) {
        return directorDAO.saveDirector(director);
    }

    public Director updateDirector(Integer id, Director director) {
        Optional<Director> directorOptional = directorDAO.getDirectorById(id);
        if (directorOptional.isPresent()) {
            director.setId(id);
            return directorDAO.saveDirector(director);
        }
        throw new IllegalArgumentException("Director with id " + id + " not found");
    }

    public boolean deleteDirector(Integer id) {
        if (directorDAO.getDirectorById(id).isPresent()) {
            directorDAO.deleteDirectorById(id);
            return true;
        }
        return false;
    }
}

package movie_app.staff_service.service;

import movie_app.staff_service.domain.dao.IScreenwriterDAO;
import movie_app.staff_service.domain.Screenwriter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScreenwriterService {
    private final IScreenwriterDAO screenwriterDAO;

    public ScreenwriterService(IScreenwriterDAO screenwriterDAO) {
        this.screenwriterDAO = screenwriterDAO;
    }

    public List<Screenwriter> getAllScreenwriters() {
        return screenwriterDAO.getAllScreenwriters();
    }

    public Optional<Screenwriter> getScreenwriterById(Integer id) {
        return screenwriterDAO.getScreenwriterById(id);
    }

    public List<Screenwriter> searchScreenwritersByName(String name) {
        return screenwriterDAO.getScreenwriterByName(name);
    }

    public Screenwriter addScreenwriter(Screenwriter screenwriter) {
        return screenwriterDAO.saveScreenwriter(screenwriter);
    }

    public Screenwriter updateScreenwriter(Integer id, Screenwriter screenwriter) {
        Optional<Screenwriter> optionalScreenwriter = screenwriterDAO.getScreenwriterById(id);
        if (optionalScreenwriter.isPresent()) {
            screenwriter.setId(id);
            return screenwriterDAO.saveScreenwriter(screenwriter);
        }
        throw new RuntimeException("Screenwriter not found with id: " + id);
    }

    public boolean deleteScreenwriter(Integer id) {
        if (screenwriterDAO.getScreenwriterById(id).isPresent()) {
            screenwriterDAO.deleteScreenwriterById(id);
            return true;
        }
        return false;
    }
}

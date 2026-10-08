package movie_app.staff_service.infrastructure;

import movie_app.staff_service.domain.dao.IDirectorDAO;
import movie_app.staff_service.domain.Director;
import movie_app.staff_service.infrastructure.repository.JpaDirectorRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class DirectorDAOImpl implements IDirectorDAO {
    private final JpaDirectorRepository jpaDirectorRepository;

    public DirectorDAOImpl(JpaDirectorRepository jpaDirectorRepository) {
        this.jpaDirectorRepository = jpaDirectorRepository;
    }

    @Override
    public List<Director> getAllDirectors() {
        return jpaDirectorRepository.findAll().stream().map(DirectorEntity::toDomain).toList();
    }

    @Override
    public Optional<Director> getDirectorById(Integer id) {
        return jpaDirectorRepository.findById(id).map(DirectorEntity::toDomain);
    }

    @Override
    public List<Director> getDirectorByName(String name) {
        return jpaDirectorRepository.findByNameContainingIgnoreCase(name).stream().map(DirectorEntity::toDomain).toList();
    }

    @Override
    public Director saveDirector(Director director) {
        DirectorEntity entityToSave = new DirectorEntity(director);
        DirectorEntity savedEntity = jpaDirectorRepository.save(entityToSave);
        return savedEntity.toDomain();
    }

    @Override
    public void deleteDirectorById(Integer id) {
        jpaDirectorRepository.deleteById(id);
    }

}

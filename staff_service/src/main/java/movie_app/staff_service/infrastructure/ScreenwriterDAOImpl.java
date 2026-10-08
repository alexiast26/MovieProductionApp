package movie_app.staff_service.infrastructure;

import movie_app.staff_service.domain.dao.IScreenwriterDAO;
import movie_app.staff_service.domain.Screenwriter;
import movie_app.staff_service.infrastructure.repository.JpaScreenwriterRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ScreenwriterDAOImpl implements IScreenwriterDAO {
    private final JpaScreenwriterRepository jpaScreenwriterRepository;

    public ScreenwriterDAOImpl(JpaScreenwriterRepository jpaScreenwriterRepository) {
        this.jpaScreenwriterRepository = jpaScreenwriterRepository;
    }

    @Override
    public List<Screenwriter> getAllScreenwriters(){
        return jpaScreenwriterRepository.findAll().stream().map(ScreenwriterEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Screenwriter> getScreenwriterById(Integer id){
        return jpaScreenwriterRepository.findById(id).map(ScreenwriterEntity::toDomain);
    }

    @Override
    public List<Screenwriter> getScreenwriterByName(String name) {
        return jpaScreenwriterRepository.findByNameContainingIgnoreCase(name).stream().map(ScreenwriterEntity::toDomain).toList();
    }

    @Override
    public Screenwriter saveScreenwriter(Screenwriter screenwriter) {
        ScreenwriterEntity screenwriterEntity = new ScreenwriterEntity(screenwriter);
        ScreenwriterEntity savedScreenwriterEntity = jpaScreenwriterRepository.save(screenwriterEntity);
        return savedScreenwriterEntity.toDomain();
    }

    @Override
    public void deleteScreenwriterById(Integer id) {
        jpaScreenwriterRepository.deleteById(id);
    }

}

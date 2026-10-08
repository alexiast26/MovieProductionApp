package com.movie_app.users_service.infrastructure;

import com.movie_app.users_service.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {    //clasa adapter
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setUserRole(user.getRole());

        UserEntity savedEntity = jpaUserRepository.save(userEntity);
        return toDomainModel(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username).map(this::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return jpaUserRepository.findById(id).map(this::toDomainModel);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return jpaUserRepository.findByUserRole(role)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    private User toDomainModel(UserEntity entity){
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPasswordHash(),
                entity.getUserRole()
        );
    }
}

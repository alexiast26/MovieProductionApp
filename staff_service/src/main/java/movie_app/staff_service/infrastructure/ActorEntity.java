package movie_app.staff_service.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movie_app.staff_service.domain.Actor;

@Entity
@Table(name = "actor")
@Getter
@Setter
@NoArgsConstructor
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int age;
    private String gender;
    @Column(columnDefinition = "LONGTEXT")
    private String image_url;


    public ActorEntity(Actor actor) {
        this.id = actor.getId();
        this.name = actor.getName();
        this.age = actor.getAge();
        this.gender = actor.getGender();
        this.image_url = actor.getImage_url();
    }

    public Actor toDomain(){
        return new Actor(this.id, this.name, this.age, this.gender, this.image_url);
    }
}

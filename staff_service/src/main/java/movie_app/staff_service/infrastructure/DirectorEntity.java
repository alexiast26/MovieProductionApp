package movie_app.staff_service.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import movie_app.staff_service.domain.Director;

@Entity
@Table(name = "director")
@Getter
@Setter
@NoArgsConstructor
public class DirectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int age;
    private String gender;
    @Column(columnDefinition = "LONGTEXT")
    private String image_url;


    public DirectorEntity(Director director) {
        this.id = director.getId();
        this.name = director.getName();
        this.age = director.getAge();
        this.gender = director.getGender();
        this.image_url = director.getImage_url();
    }

    public Director toDomain(){
        return new Director(this.id, this.name, this.age, this.gender, this.image_url);
    }
}

package movie_app.staff_service.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import movie_app.staff_service.domain.Screenwriter;

@Entity
@Table(name = "screenwriter")
@Getter
@Setter
@NoArgsConstructor
public class ScreenwriterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int age;
    private String gender;
    @Column(columnDefinition = "LONGTEXT")
    private String image_url;


    public ScreenwriterEntity(Screenwriter screenwriter) {
        this.id = screenwriter.getId();
        this.name = screenwriter.getName();
        this.age = screenwriter.getAge();
        this.gender = screenwriter.getGender();
        this.image_url = screenwriter.getImage_url();
    }

    public Screenwriter toDomain(){
        return new Screenwriter(this.id, this.name, this.age, this.gender, this.image_url);
    }
}

package movie_app.staff_service.domain;


public class Director {
    private Integer id;
    private String name;
    private int age;
    private String gender;
    private String image_url;

    public Director() {
    }

    public Director(String name, int age, String gender, String image_url) {
        this.id = null;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.image_url = image_url;
    }

    public Director(Integer id, String name, int age, String gender, String image_url) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.image_url = image_url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

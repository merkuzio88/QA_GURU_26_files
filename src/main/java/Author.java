import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {

    @JsonProperty("name")
    private String name;

    @JsonProperty("yearOfBirth")
    private Integer yearOfBirth;

    public Author() {
    }

    public Author(String name, Integer yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}

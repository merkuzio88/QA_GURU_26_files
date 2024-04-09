import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class BookTest {

    @Test
    void bookTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("book.json");
        Book warAndPeace = objectMapper.readValue(inputStream, Book.class);
        System.out.println(warAndPeace.getTitle());
        checkAuthorName(warAndPeace, 1828);
    }

    public void checkAuthorName(Book book, Integer yearOfBirth) {
        Assertions.assertEquals(book.getAuthor().getYearOfBirth(), yearOfBirth);
    }
}

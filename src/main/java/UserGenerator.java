import static java.util.UUID.randomUUID;

public class UserGenerator {
    public static User randomUser() {

        return new User()
                .setEmail(randomUUID().toString() + "@yandex.ru")
                .setPassword(randomUUID().toString())
                .setName(randomUUID().toString());
    }
}

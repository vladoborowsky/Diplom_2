import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRegistrationNegativeTest {
    User user;

    @Before
    public void setUp() {
        user = UserGenerator.randomUser();
    }

    @Test
    @DisplayName("Создание нового пользователя не передав email")
    public void createUserWithoutEmail() {
        ValidatableResponse createNewResponse = UserClient.create(user, "email");
        assertEquals("Неверный код ответа", 403, createNewResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, createNewResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "Email, password and name are required fields", createNewResponse.extract().path("message"));
    }

    @Test
    @DisplayName("Создание нового пользователя не передав пароль")
    public void createUserWithoutPassword() {
        ValidatableResponse createNewResponse = UserClient.create(user, "password");
        assertEquals("Неверный код ответа", 403, createNewResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, createNewResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "Email, password and name are required fields", createNewResponse.extract().path("message"));
    }

    @Test
    @DisplayName("Создание нового пользователя не передав имя")
    public void createUserWithoutName() {
        ValidatableResponse createNewResponse = UserClient.create(user, "name");
        assertEquals("Неверный код ответа", 403, createNewResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, createNewResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "Email, password and name are required fields", createNewResponse.extract().path("message"));
    }
}

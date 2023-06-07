import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserRegistrationPositiveTest {
    User user;
    ValidatableResponse createResponse;
    String accessToken;


    @Before
    public void setUp() {
        user = UserGenerator.randomUser();
        createResponse = UserClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void createUser() {
        assertEquals("Неверный код ответа", 200, createResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, createResponse.extract().path("success"));
        assertEquals("Несоответствие email в теле ответа", user.getEmail(), createResponse.extract().path("user.email"));
        assertEquals("Несоответствие name в теле ответа", user.getName(), createResponse.extract().path("user.name"));
        assertNotNull("Отсутствие accessToken в теле ответа", createResponse.extract().path("accessToken"));
        assertNotNull("Отсутствие refreshToken в теле ответа", createResponse.extract().path("refreshToken"));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void createIdenticalUser() {
        ValidatableResponse createNewResponse = UserClient.create(user);
        assertEquals("Неверный код ответа", 403, createNewResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, createNewResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "User already exists", createNewResponse.extract().path("message"));
    }

    @After
    public void tearDown() {
        UserClient.delete(accessToken);
    }
}

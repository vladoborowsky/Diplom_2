import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserAuthorizationTest {
    User user;
    String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.randomUser();
        ValidatableResponse createResponse = UserClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginUser() {
        ValidatableResponse loginResponse = UserClient.login(user);
        assertEquals("Неверный код ответа", 200, loginResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, loginResponse.extract().path("success"));
        assertEquals("Несоответствие email в теле ответа", user.getEmail(), loginResponse.extract().path("user.email"));
        assertEquals("Несоответствие name в теле ответа", user.getName(), loginResponse.extract().path("user.name"));
        assertNotNull("Отсутствие accessToken в теле ответа", loginResponse.extract().path("accessToken"));
        assertNotNull("Отсутствие refreshToken в теле ответа", loginResponse.extract().path("refreshToken"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    public void loginUserWrongLogin() {
        User randomUser = UserGenerator.randomUser();
        randomUser.setPassword(user.getPassword());
        ValidatableResponse loginResponse = UserClient.login(randomUser);
        assertEquals("Неверный код ответа", 401, loginResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, loginResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "email or password are incorrect", loginResponse.extract().path("message"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    public void loginUserWrongPassword() {
        User randomUser = UserGenerator.randomUser();
        randomUser.setEmail(user.getEmail());
        ValidatableResponse loginResponse = UserClient.login(randomUser);
        assertEquals("Неверный код ответа", 401, loginResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, loginResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "email or password are incorrect", loginResponse.extract().path("message"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    public void loginUserWrongLoginAndPassword() {
        User randomUser = UserGenerator.randomUser();
        ValidatableResponse loginResponse = UserClient.login(randomUser);
        assertEquals("Неверный код ответа", 401, loginResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, loginResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "email or password are incorrect", loginResponse.extract().path("message"));
    }

    @After
    public void tearDown() {
        UserClient.delete(accessToken);
    }
}

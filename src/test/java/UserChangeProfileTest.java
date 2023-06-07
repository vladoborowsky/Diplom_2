import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserChangeProfileTest {
    User user;
    String accessToken;
    User randomUser;


    @Before
    public void setUp() {
        user = UserGenerator.randomUser();
        ValidatableResponse createResponse = UserClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        randomUser = UserGenerator.randomUser();
    }

    @Test
    @DisplayName("Изменение логина пользователя")
    public void changeUserLogin() {
        user.setEmail(randomUser.getEmail());
        ValidatableResponse changeUserResponse = UserClient.update(accessToken, user);
        assertEquals("Неверный код ответа", 200, changeUserResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, changeUserResponse.extract().path("success"));
        assertEquals("Несоответствие email в теле ответа", user.getEmail(), changeUserResponse.extract().path("user.email"));
        assertEquals("Несоответствие name в теле ответа", user.getName(), changeUserResponse.extract().path("user.name"));
    }


    @Test
    @DisplayName("Изменение имени пользователя")
    public void changeUserName() {
        user.setName(randomUser.getName());
        ValidatableResponse changeUserResponse = UserClient.update(accessToken, user);
        assertEquals("Неверный код ответа", 200, changeUserResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, changeUserResponse.extract().path("success"));
        assertEquals("Несоответствие email в теле ответа", user.getEmail(), changeUserResponse.extract().path("user.email"));
        assertEquals("Несоответствие name в теле ответа", user.getName(), changeUserResponse.extract().path("user.name"));
    }

    @Test
    @DisplayName("Изменение логина пользователя без авторизации")
    public void changeUserLoginWithoutAuthorization() {
        user.setEmail(randomUser.getEmail());
        ValidatableResponse changeUserResponse = UserClient.update(null, user);
        assertEquals("Неверный код ответа", 401, changeUserResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, changeUserResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "You should be authorised", changeUserResponse.extract().path("message"));
    }


    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorization() {
        user.setName(randomUser.getName());
        ValidatableResponse changeUserResponse = UserClient.update(null, user);
        assertEquals("Неверный код ответа", 401, changeUserResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, changeUserResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "You should be authorised", changeUserResponse.extract().path("message"));
    }

    @After
    public void tearDown() {
        UserClient.delete(accessToken);
    }
}

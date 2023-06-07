import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Создание нового пользователя")
    public static ValidatableResponse create(User user) {
        return create(user, null);
    }

    @Step("Создание нового пользователя")
    public static ValidatableResponse create(User user, String excludeField) {
        JsonObject jsonObject = SerializationUtils.excludeFieldSerialization(user, excludeField);

        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(jsonObject)
                .post("/api/auth/register")
                .then();
    }

    @Step("Удаление пользователя")
    public static ValidatableResponse delete(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .delete("/api/auth/user")
                .then();
    }

    @Step("Авторизация пользователя")
    public static ValidatableResponse login(User user) {
        JsonObject jsonObject = SerializationUtils.excludeFieldSerialization(user, "name");

        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(jsonObject)
                .post("/api/auth/login")
                .then();
    }

    @Step("Обновление данных пользователя")
    public static ValidatableResponse update(String token, User user) {
        JsonObject jsonObject = SerializationUtils.excludeFieldSerialization(user, "password");
        RequestSpecification requestSpecification = given();
        if (token != null) {
            requestSpecification.header("Authorization", token);
        }
        return requestSpecification
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(jsonObject)
                .patch("/api/auth/user")
                .then();
    }
}
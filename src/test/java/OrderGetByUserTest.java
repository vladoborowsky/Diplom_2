import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderGetByUserTest {
    User user;
    String accessToken;
    List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        user = UserGenerator.randomUser();
        ValidatableResponse createResponse = UserClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        ValidatableResponse getResponse = OrderClient.getIngredients();
        String firstIngredient = getResponse.extract().path("data[0]._id");
        String secondIngredient = getResponse.extract().path("data[1]._id");
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        Order order = new Order();
        order.setIngredients(ingredients);
        OrderClient.create(accessToken, order);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    public void getOrderByUser() {
        ValidatableResponse getResponse = OrderClient.getByUser(accessToken);
        assertEquals("Неверный код ответа", 200, getResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, getResponse.extract().path("success"));
        String firstIngredient = getResponse.extract().path("orders[0].ingredients[0]");
        assertTrue("Несоответствие ингридиентов в теле ответа", ingredients.contains(firstIngredient));
        String secondIngredient = getResponse.extract().path("orders[0].ingredients[1]");
        assertTrue("Несоответствие ингридиентов в теле ответа", ingredients.contains(secondIngredient));

    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void getOrderByUserWithoutAuthorization() {
        ValidatableResponse getResponse = OrderClient.getByUser(null);
        assertEquals("Неверный код ответа", 401, getResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, getResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "You should be authorised", getResponse.extract().path("message"));

    }

    @After
    public void tearDown() {
        UserClient.delete(accessToken);
    }
}
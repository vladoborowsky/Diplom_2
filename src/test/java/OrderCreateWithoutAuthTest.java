import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;

public class OrderCreateWithoutAuthTest {

    @Test
    @DisplayName("Создание нового заказа")
    public void createOrder() {
        ValidatableResponse getResponse = OrderClient.getIngredients();
        String firstIngredient = getResponse.extract().path("data[0]._id");
        String secondIngredient = getResponse.extract().path("data[1]._id");
        List<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);
        Order order = new Order();
        order.setIngredients(ingredients);

        ValidatableResponse createResponse = OrderClient.create(null, order);
        assertEquals("Неверный код ответа", 200, createResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", true, createResponse.extract().path("success"));
    }

    @Test
    @DisplayName("Создание нового заказа c пустым списком ингридиентов")
    public void createOrderWithoutIngredientsHash() {
        List<String> ingredients = new ArrayList<>();
        Order order = new Order();
        order.setIngredients(ingredients);

        ValidatableResponse createResponse = OrderClient.create(null, order);
        assertEquals("Неверный код ответа", 400, createResponse.extract().statusCode());
        assertEquals("Неверный статус в теле ответа", false, createResponse.extract().path("success"));
        assertEquals("Неверное описание в теле ответа", "Ingredient ids must be provided", createResponse.extract().path("message"));
    }

    @Test
    @DisplayName("Создание нового заказа c неверным хешем ингридиентов")
    public void createOrderWithWrongIngredientsHash() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(randomUUID().toString());
        ingredients.add(randomUUID().toString());
        Order order = new Order();
        order.setIngredients(ingredients);

        ValidatableResponse createResponse = OrderClient.create(null, order);
        assertEquals("Неверный код ответа", 500, createResponse.extract().statusCode());
    }

}
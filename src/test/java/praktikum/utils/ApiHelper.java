package praktikum.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import praktikum.models.LoginCredentials;
import praktikum.models.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ApiHelper {
    // Явно указываем рабочий BASE_URI
    private static final String BASE_URI = "https://stellarburgers.education-services.ru";

    static {
        // RestAssured базовый URI — добавляем /api здесь, чтобы дальше использовать относительные пути
        RestAssured.baseURI = BASE_URI + "/api";
    }

    public static ValidatableResponse registerUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/auth/register")
                .then();
    }

    public static ValidatableResponse loginUser(LoginCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then();
    }

    public static Response deleteUser(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken) // ожидается "Bearer <token>" или просто токен в зависимости от API
                .when()
                .delete("/auth/user");
    }

    public static String extractAccessToken(ValidatableResponse loginResponse) {
        loginResponse.assertThat().body("accessToken", notNullValue());
        String fullToken = loginResponse.extract().path("accessToken");
        if (fullToken != null && fullToken.startsWith("Bearer ")) {
            return fullToken.substring(7);
        }
        return fullToken;
    }
}
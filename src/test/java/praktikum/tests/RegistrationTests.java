package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.pages.LoginPage;
import praktikum.pages.RegistrationPage;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;

import static org.junit.Assert.assertTrue;

@DisplayName("Тесты регистрации")
public class RegistrationTests extends BaseTest {

    private String accessTokenForCleanup;

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка успешной регистрации пользователя с уникальными данными")
    public void testSuccessfulRegistration() {
        String email = TestUtils.generateRandomEmail();
        String password = TestUtils.generateRandomPassword();
        String name = TestUtils.generateRandomName();
        User testUser = new User(email, password, name);

        // Переход на страницу регистрации
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();

        // Регистрация пользователя
        registrationPage.register(testUser.getName(), testUser.getEmail(), testUser.getPassword());

        // Проверка перехода на страницу входа
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        assertTrue("Не произошел переход на страницу входа после успешной регистрации",
                loginPage.isLoginFormDisplayed());

        // Сохраняем токен для очистки в методе @After
        var loginResponse = ApiHelper.loginUser(new LoginCredentials(email, password));
        if (loginResponse.extract().statusCode() == 200) {
            accessTokenForCleanup = ApiHelper.extractAccessToken(loginResponse);
        }
    }

    @Test
    @DisplayName("Ошибка для некорректного пароля")
    @Description("Проверка ошибки при регистрации с паролем менее 6 символов")
    public void testRegistrationWithShortPassword() {
        String email = TestUtils.generateRandomEmail();
        String password = "123"; // Некорректный пароль
        String name = TestUtils.generateRandomName();

        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.register(name, email, password);

        assertTrue("Не отображается ошибка валидации пароля 'Некорректный пароль'",
                registrationPage.isErrorDisplayed());
    }

    @After
    public void tearDown() {
        // Очистка: удаление пользователя после теста
        if (accessTokenForCleanup != null && !accessTokenForCleanup.isEmpty()) {
            try {
                var deleteResponse = ApiHelper.deleteUser("Bearer " + accessTokenForCleanup);
                System.out.println("Пользователь удалён, статус: " + deleteResponse.getStatusCode());
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
        super.tearDown();
    }
}
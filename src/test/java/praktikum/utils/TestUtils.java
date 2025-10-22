package praktikum.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestUtils {

    private static final Properties properties = new Properties();
    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final Logger logger = Logger.getLogger(TestUtils.class.getName());

    static {
        try (InputStream input = TestUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.warning("⚠️  Файл application.properties не найден, будут использованы значения по умолчанию.");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Ошибка при загрузке properties", ex);
        }
    }

    // Получение базового URL
    public static String getBaseUrl() {
        return properties.getProperty("base.url", "https://stellarburgers.education-services.ru");
    }

    // Получение типа браузера (приоритет — System property, потом файл)
    public static String getBrowserType() {
        return System.getProperty("browser.type", properties.getProperty("browser.type", "chrome"));
    }

    // Создание WebDriver для Chrome или Yandex
    public static WebDriver createDriver() {
        String browserType = getBrowserType().toLowerCase();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (browserType.equals("yandex")) {
            // путь к YandexBrowser.exe
            String yandexPath = "C:\\Users\\Nur\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe";
            options.setBinary(yandexPath);

            // Устанавливаем драйвер под версию Chromium Яндекса
            WebDriverManager.chromedriver().driverVersion("138.0.7204.96").setup();
            logger.info("✅ Запуск тестов в Яндекс.Браузере");
        } else {
            // Автоматический подбор драйвера для Google Chrome
            WebDriverManager.chromedriver().setup();
            logger.info("✅ Запуск тестов в Google Chrome");
        }

        return new ChromeDriver(options);
    }

    // Методы генерации данных (JavaFaker)
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 15, true, true);
    }
}
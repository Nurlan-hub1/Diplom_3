package praktikum.tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import praktikum.utils.TestUtils;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;

    @Before
    public void setUp() {
        driver = TestUtils.createDriver();
        driver.manage().window().maximize();

        // Устанавливаем неявное ожидание один раз для всех тестов
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        baseUrl = TestUtils.getBaseUrl();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
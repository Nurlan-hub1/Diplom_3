package praktikum.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Базовый класс для всех PageObject-страниц.
 * Содержит общие методы и обработку типовых исключений.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Явное ожидание по умолчанию
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Клик по элементу с повторными попытками (без Thread.sleep)
     */
    public void clickWithRetries(By locator) {
        int attempts = 0;
        final int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                return; // успешно кликнули
            } catch (ElementClickInterceptedException e) {
                System.out.println("ElementClickInterceptedException: повторная попытка (" + (attempts + 1) + ")");
                try {
                    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception ignored) {
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException: элемент устарел, пробуем снова (" + (attempts + 1) + ")");
            } catch (TimeoutException e) {
                System.out.println("TimeoutException: элемент не найден (" + locator + ")");
            } catch (Exception e) {
                System.err.println("Неожиданная ошибка при клике: " + e.getMessage());
            }
            attempts++;
        }
        throw new RuntimeException("Не удалось кликнуть элемент после " + maxAttempts + " попыток: " + locator);
    }
    /**
     * Универсальный клик по элементу (использует clickWithRetries)
     */
    public void click(By locator) {
        clickWithRetries(locator);
    }

    /**
     * Ввод текста в элемент (с ожиданием кликабельности)
     */
    public void sendKeys(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Получение текста элемента
     */
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Ожидание видимости элемента
     */
    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Проверка отображения элемента
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
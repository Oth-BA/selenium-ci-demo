package com.qa.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class GoogleSearchTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @Test
    public void loginPageTitleIsCorrect() {
        // Step 1: navigate to the login page
        driver.get("https://the-internet.herokuapp.com/login");

        // Step 2: wait for page to load and verify the title
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleIs("The Internet"));

        Assert.assertEquals(
                driver.getTitle(),
                "The Internet",
                "Page title mismatch"
        );
    }

    @Test
    public void loginWithValidCredentials() {
        // Step 1: navigate to login page
        driver.get("https://the-internet.herokuapp.com/login");

        // Step 2: enter valid credentials
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        username.sendKeys("tomsmith");

        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Step 3: verify successful login message
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("flash"))
        );
        Assert.assertTrue(
                successMessage.getText().contains("You logged into a secure area!"),
                "Success message not found. Actual: " + successMessage.getText()
        );
    }

    @Test
    public void loginWithInvalidCredentials() {
        // Step 1: navigate to login page
        driver.get("https://the-internet.herokuapp.com/login");

        // Step 2: enter invalid credentials
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        username.sendKeys("wronguser");

        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Step 3: verify error message is shown
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("flash"))
        );
        Assert.assertTrue(
                errorMessage.getText().contains("Your username is invalid!"),
                "Error message not found. Actual: " + errorMessage.getText()
        );
    }
    @Test
    public void secureAreaHasLogoutButton() {
        // Step 1: log in first
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Step 2: verify we landed on the secure area
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("secure"));

        // Step 3: verify the logout button exists
        WebElement logoutButton = driver.findElement(
                By.cssSelector("a.button.secondary")
        );
        Assert.assertTrue(
                logoutButton.isDisplayed(),
                "Logout button should be visible on secure area"
        );
        Assert.assertTrue(
                logoutButton.getText().contains("Logout"),
                "Button text should say Logout but was: " + logoutButton.getText()
        );
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
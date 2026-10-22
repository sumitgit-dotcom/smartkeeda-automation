package com.smartkeeda.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class Hooks {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        
        // Detect if running in GitHub Actions
        boolean isCI = System.getenv("CI") != null;
        
        if (isCI) {
            options.addArguments("--headless=new"); // Required for CI
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        
        options.addArguments("--disable-notifications");
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        DRIVER.set(driver);
        System.out.println(">>> Browser opened. CI Mode: " + isCI);
    }

    @After
    public void tearDown() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
            System.out.println(">>> Browser closed.");
        }
    }
}
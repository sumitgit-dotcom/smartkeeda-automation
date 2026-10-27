package com.smartkeeda.steps;

import com.smartkeeda.hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FreeMockSteps {
	
    private final WebDriver driver = Hooks.getDriver();

    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    @When("I click the Free Mock button")
    public void iClickTheFreeMockButton() {
        By freeMockLocator = By.xpath("//button[@class='free-mock']");
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(freeMockLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println(">>> Clicked Free Mock button.");
            Thread.sleep(3000); // Wait for the next page to load
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click the Free Mock button.", e);
        }
    }
    
    
    @When("I click on the {string} tab")
    public void iClickOnTheTab(String tabName) {
        By tabLocator = By.xpath("//div[normalize-space()='" + tabName + "']");
        try {
            WebElement tab = wait.until(ExpectedConditions.presenceOfElementLocated(tabLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
            System.out.println(">>> Clicked tab: " + tabName);
            Thread.sleep(2500);
        } catch (Exception e) {
            throw new RuntimeException("Could not find the tab: " + tabName, e);
        }
    }
    
    @When("I click the first card in the tabs wrapper")
    public void iClickTheFirstCardInTheTabsWrapper() {
        By cardLocator = By.xpath("//div[@class='tabs-wrapper']//div[@class='card']");
        try {
            List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardLocator));
            
 if (cards.isEmpty()) throw new RuntimeException("No cards found in tabs-wrapper.");
            
            WebElement firstCard = cards.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCard);
            System.out.println(">>> Clicked first card in tabs-wrapper.");
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException("Could not click the first card.", e);
        }
    }
    
    
    @When("I click on the LIC AAO Mock Test card")
    public void iClickOnTheLicAaoMockTestCard() {
        By licLocator = By.xpath("//a[@href='/lic-aao-mock-test-series']//div[@class='inner-card']//div[@class='content']");
        try {
            WebElement licCard = wait.until(ExpectedConditions.presenceOfElementLocated(licLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", licCard);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", licCard);
            System.out.println(">>> Clicked LIC AAO Mock Test card.");
            Thread.sleep(4000); // Dashboard can take a moment to load
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click the LIC AAO Mock Test card.", e);
        }
    }
    
    
    @When("I start the first test on the dashboard")
    public void iStartTheFirstTestOnTheDashboard() {
        // Your provided absolute XPath
        String absoluteXPath = "/html/body/app-root/div[@class='main-container']/app-dashboard-template/main/div[@class='main-content']/div[@class='content']/app-feature-screen/section[@class='section']/div[@class='test-1 content-grid']/div[@class='card-1']/div[1]/app-test-card[1]/div[1]/div[2]/a[1]";
        By startBtnLocator = By.xpath(absoluteXPath);

        try {
            WebElement startBtn = wait.until(ExpectedConditions.presenceOfElementLocated(startBtnLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", startBtn);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startBtn);
            System.out.println(">>> Started the first test on the dashboard.");
            Thread.sleep(5000); // Wait for the test interface to load
        } catch (Exception e) {
            // Fallback: If absolute XPath fails, find the first 'Start' or 'Resume' link
            System.out.println(">>> Absolute XPath failed. Trying fallback locator...");
            try {
                By fallback = By.xpath("//app-test-card[1]//a[contains(text(), 'Start') or contains(text(), 'Resume') or contains(@class, 'btn')]");
                WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(fallback));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                System.out.println(">>> Started the test using fallback locator.");
                Thread.sleep(5000);
            } catch (Exception ex) {
                throw new RuntimeException("Could not start the test. The dashboard layout may have changed.", ex);
            }
        }
    }
    
    
    @Then("I print the test results")
    public void iPrintTheTestResults() {
        try {
            // Wait a few seconds for the test page or result modal to fully render
            Thread.sleep(3000);
            System.out.println("\n==================================================");
            System.out.println("           TEST DETAILS / RESULTS                 ");
            System.out.println("==================================================");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title:  " + driver.getTitle());
            
            
            List<WebElement> headings = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //div[contains(@class, 'title') or contains(@class, 'result')]"));
            int count = 1;
            for (WebElement heading : headings) {
                String text = heading.getText().trim();
                if (!text.isEmpty() && text.length() < 100) {
                    System.out.println("   " + count + ". " + text);
                    count++;
                }
            }
            System.out.println("==================================================\n");
            
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract or print test results.", e);
        }
    }

}

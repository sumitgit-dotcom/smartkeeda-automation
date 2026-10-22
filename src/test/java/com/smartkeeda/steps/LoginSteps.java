package com.smartkeeda.steps;

import com.smartkeeda.hooks.Hooks;
import io.cucumber.java.en.Given;
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

public class LoginSteps {
    private final WebDriver driver = Hooks.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    @Given("I navigate to the Testzone homepage")
    public void iNavigateToTheTestzoneHomepage() {
        driver.get("https://testzone.smartkeeda.com/");
        
        // Wait for the React app to finish loading
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Loading...')]")));
                
        System.out.println(">>> Homepage loaded: " + driver.getTitle());
    }

    @When("I click on the Log In button")
    public void iClickOnTheLogInButton() {
        // 1. Check if the element is inside an iframe
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (!iframes.isEmpty()) {
            System.out.println(">>> Found " + iframes.size() + " iframes. Switching to first iframe.");
            driver.switchTo().frame(iframes.get(0));
        }

        // 2. Use a broader XPath that looks for any element containing "Log In"
        By loginLocator = By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'log in')] | //*[contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'log in')]");
        
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.presenceOfElementLocated(loginLocator));
            
            // Scroll into view and use JS click to bypass overlapping elements
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", loginBtn);
            Thread.sleep(500); // Small pause for animations
            
            try {
                loginBtn.click();
            } catch (Exception e) {
                System.out.println(">>> Standard click failed, using JS click.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);
            }
            
            System.out.println(">>> Clicked Log In button.");
            
        } catch (Exception e) {
            // If we switched into an iframe and failed, switch back before throwing
            driver.switchTo().defaultContent();
            throw new RuntimeException("Could not find the Log In button. Check if the site layout has changed.", e);
        }
    }

    @Then("the login modal should appear")
    public void theLoginModalShouldAppear() {
        try {
            // Check for the modal text
            boolean modalVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Get 1000+ Free Mocks')]"))).isDisplayed();
                    
            if (modalVisible) {
                System.out.println(">>> Login modal appeared successfully.");
            }
        } catch (Exception e) {
            throw new AssertionError("Login modal failed to appear.");
        } finally {
            // Always switch back to main content if we were in an iframe
            driver.switchTo().defaultContent();
        }
    }
}
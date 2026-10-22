package com.smartkeeda.steps;

import com.smartkeeda.hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;


import java.time.Duration;
import java.util.List;
import java.util.Map;

public class LoginSteps {
    private final WebDriver driver = Hooks.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // ==========================================
    // NAVIGATION & LOGIN
    // ==========================================

    @Given("I navigate to the Testzone homepage")
    public void iNavigateToTheTestzoneHomepage() {
        driver.get("https://testzone.smartkeeda.com/");
        
        try {
            // Wait for the React app to finish loading (up to 30 seconds)
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Loading...')]")));
            System.out.println(">>> Homepage fully loaded.");
        } catch (Exception e) {
            System.out.println(">>> Homepage loaded, but 'Loading...' text wasn't found (it may have loaded too fast). Proceeding...");
        }
        
        System.out.println(">>> Title: " + driver.getTitle());
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
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", loginBtn);
            Thread.sleep(500); 
            
            try {
                loginBtn.click();
            } catch (Exception e) {
                System.out.println(">>> Standard click failed, using JS click.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);
            }
            
            System.out.println(">>> Clicked Log In button.");
            
        } catch (Exception e) {
            driver.switchTo().defaultContent();
            throw new RuntimeException("Could not find the Log In button.", e);
        }
    }

    @Then("the login modal should appear")
    public void theLoginModalShouldAppear() {
        try {
            boolean modalVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Get 1000+ Free Mocks')]"))).isDisplayed();
                    
            if (modalVisible) {
                System.out.println(">>> Login modal appeared successfully.");
            }
        } catch (Exception e) {
            throw new AssertionError("Login modal failed to appear.");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    // ==========================================
    // SIGN UP FLOW
    // ==========================================

    @When("I click on the Sign In button")
    public void iClickOnTheSignInButton() {
        By signInLocator = By.xpath("//button[normalize-space()='Sign In']");
        WebElement signInBtn = wait.until(ExpectedConditions.elementToBeClickable(signInLocator));
        
        try {
            signInBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInBtn);
        }
        System.out.println(">>> Clicked Sign In button. Waiting for popup to load...");
        
        // Wait for the login popup to fully render (adjust if the text is different)
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }


    @When("I click on the SignUp Now button")
    public void iClickOnTheSignUpNowButton() {
        // Wait for the Sign In modal to fully render
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        // Find the SignUp Now element
        By signUpLocator = By.xpath("//*[self::a or self::button or self::span or self::div][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'signup') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign up')]");
        
        try {
            WebElement signUpBtn = wait.until(ExpectedConditions.presenceOfElementLocated(signUpLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", signUpBtn);
            Thread.sleep(1000);

            // Use Actions class to physically move the mouse and click (more reliable for React)
            org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
            actions.moveToElement(signUpBtn).pause(java.time.Duration.ofMillis(500)).click().perform();
            
            System.out.println(">>> Clicked SignUp Now button using Actions class.");
            Thread.sleep(3000); // Wait for UI to transition
            
        } catch (Exception e) {
            // Fallback to JS click if Actions fails
            System.out.println(">>> Actions click failed, trying JS click...");
            try {
                WebElement fallbackBtn = driver.findElement(signUpLocator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fallbackBtn);
                Thread.sleep(3000);
            } catch (Exception ex) {
                throw new RuntimeException("Could not find or click the SignUp Now button.", ex);
            }
        }
    }

  

    @When("I fill in the signup details:")
    public void iFillInTheSignupDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        System.out.println(">>> Attempting to fill the form...");

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // 1. Enter Full Name (using flexible placeholder matching)
            By nameLocator = By.xpath("//input[contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'name')]");
            WebElement nameField = longWait.until(ExpectedConditions.visibilityOfElementLocated(nameLocator));
            nameField.clear();
            nameField.sendKeys(data.get("name"));
            System.out.println(">>> Entered Full Name.");

            // 2. Enter Email/Phone (using flexible matching for email, mobile, or phone)
            By emailLocator = By.xpath("//input[contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'email') or contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'mobile') or contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'phone')]");
            WebElement emailField = longWait.until(ExpectedConditions.visibilityOfElementLocated(emailLocator));
            emailField.clear();
            emailField.sendKeys(data.get("email"));
            System.out.println(">>> Entered Email/Phone.");

            // 3. Enter Password
            By passLocator = By.xpath("//input[@type='password' or contains(@placeholder, 'Password')]");
            WebElement passField = longWait.until(ExpectedConditions.visibilityOfElementLocated(passLocator));
            passField.clear();
            passField.sendKeys(data.get("password"));
            System.out.println(">>> Entered Password.");

            // 4. Enter Confirm Password
            By confirmPassLocator = By.xpath("//input[contains(@placeholder, 'Confirm') or contains(@placeholder, 'Re-enter')]");
            WebElement confirmPassField = longWait.until(ExpectedConditions.visibilityOfElementLocated(confirmPassLocator));
            confirmPassField.clear();
            confirmPassField.sendKeys(data.get("confirm"));
            System.out.println(">>> Entered Confirm Password.");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to find form fields. The signup form may not have loaded or the placeholders are different.", e);
        }
    }

    @Then("I should be successfully registered or see a validation message")
    public void iShouldBeSuccessfullyRegisteredOrSeeAValidationMessage() {
        try {
            // Wait for a success message, a validation error (e.g., "email already exists"), or a dashboard redirect
            boolean result = wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'success') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'already')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'error') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'invalid')]"))
            )) != null;
            
            if (result) {
                System.out.println(">>> Registration attempt completed. Success or validation message is visible.");
            }
        } catch (Exception e) {
            throw new AssertionError("No response from the server after clicking signup. The page might be stuck.");
        }
    }}
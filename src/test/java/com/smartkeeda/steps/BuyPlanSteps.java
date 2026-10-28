package com.smartkeeda.steps;

import com.smartkeeda.hooks.Hooks;
import com.smartkeeda.utils.ExcelUtils;
import io.cucumber.java.en.And;
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

public class BuyPlanSteps {

    private final WebDriver driver = Hooks.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    @Given("I navigate to the Testzone dashboard")
    public void iNavigateToTheTestzoneDashboard() {
        driver.get("https://testzone.smartkeeda.com/dashboard/bank-and-insurance-exams");
        
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Loading...')]")));
            Thread.sleep(2000);
        } catch (Exception ignored) {}
        
        System.out.println(">>> Dashboard loaded: " + driver.getTitle());
    }

    @When("I click on the {string} link")
    public void iClickOnTheLink(String linkText) {
        By locator = By.xpath("//span[normalize-space()='" + linkText + "'] | //a[normalize-space()='" + linkText + "']");
        try {
            WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
            System.out.println(">>> Clicked link: " + linkText);
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click link: " + linkText, e);
        }
    }

    @And("I click the {string} button on the first plan card")
    public void iClickTheButtonOnTheFirstPlanCard(String buttonText) throws InterruptedException {
        By locator = By.xpath("//div[@class='a-plan-card']//button[@class='a-plan-button'][normalize-space()='" + buttonText + "']");
        
        int attempts = 0;
        while (attempts < 3) {
            try {
                // 1. Wait for the buttons to be present
                List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
                if (buttons.isEmpty()) throw new RuntimeException("No plan cards found.");
                
                // 2. Grab the first button
                WebElement selectBtn = buttons.get(0);
                
                // 3. Scroll to it
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", selectBtn);
                Thread.sleep(1000); // Let any smooth-scrolling finish
                
                // 4. Try to click it
                try {
                    selectBtn.click();
                } catch (Exception e) {
                    // If standard click fails, force JS click
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectBtn);
                }
                
                System.out.println(">>> Clicked '" + buttonText + "' on the first plan card.");
                Thread.sleep(3000); // Wait for checkout modal
                return; // Success! Exit the loop.
                
            } catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.ElementClickInterceptedException e) {
                attempts++;
                System.out.println(">>> Element went stale or was intercepted. Retrying... (Attempt " + attempts + ")");
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            }
        }
        
        throw new RuntimeException("Could not click the Select Plan button after 3 attempts.");
    }

    @And("I enter the checkout email from excel")
    public void iEnterTheCheckoutEmailFromExcel() {
        String email = com.smartkeeda.utils.ExcelUtils.getCellData(1, 0);
        System.out.println(">>> Excel read email: [" + email + "]");
        
        By locator = By.xpath("//input[@placeholder='Enter Email Id']");
        try {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            emailField.clear();
            
            // Standard typing
            emailField.sendKeys(email);
            
            // If standard typing didn't work, force it via JavaScript
            if (emailField.getAttribute("value") == null || emailField.getAttribute("value").isEmpty()) {
                System.out.println(">>> Standard sendKeys failed. Forcing value via JS...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value = arguments[1];", emailField, email);
                js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", emailField);
            }
            
            System.out.println(">>> Successfully set email field.");
        } catch (Exception e) {
            throw new RuntimeException("Could not enter email.", e);
        }
    }


    @And("I click the password input wrapper")
    public void iClickThePasswordInputWrapper() {
        By locator = By.xpath("//div[@class='input-shell password-wrap']");
        try {
            WebElement wrapper = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wrapper);
            System.out.println(">>> Clicked password input wrapper.");
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click the password wrapper.", e);
        }
    }

    @And("I enter the password from excel in the checkout form")
    public void iEnterThePasswordFromExcelInTheCheckoutForm() {
        String password = com.smartkeeda.utils.ExcelUtils.getCellData(1, 1);
        System.out.println(">>> Excel read password: [" + password + "]");
        
        By locator = By.xpath("//input[@type='password'] | //div[contains(@class,'password-wrap')]//input");
        try {
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            passwordField.clear();
            
            // Standard typing
            passwordField.sendKeys(password);
            
            // If standard typing didn't work, force it via JavaScript
            if (passwordField.getAttribute("value") == null || passwordField.getAttribute("value").isEmpty()) {
                System.out.println(">>> Standard sendKeys failed. Forcing value via JS...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value = arguments[1];", passwordField, password);
                js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", passwordField);
            }
            
            System.out.println(">>> Successfully set password field.");
        } catch (Exception e) {
            throw new RuntimeException("Could not enter password.", e);
        }
    }

    @And("I click the primary submit button")
    public void iClickThePrimarySubmitButton() {
        By locator = By.xpath("//button[@class='primary-btn']");
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            
            System.out.println(">>> Clicked primary submit button.");
            Thread.sleep(4000);
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click the primary submit button.", e);
        }
    }

    @Then("I print the checkout result")
    public void iPrintTheCheckoutResult() {
        try {
            System.out.println("\n==================================================");
            System.out.println("           CHECKOUT / PAYMENT RESULT               ");
            System.out.println("==================================================");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title:  " + driver.getTitle());
            
            List<WebElement> messages = driver.findElements(By.xpath("//*[contains(@class, 'error') or contains(@class, 'success') or contains(@class, 'alert') or contains(@class, 'toast')]"));
            if (!messages.isEmpty()) {
                for (WebElement msg : messages) {
                    if (msg.isDisplayed() && !msg.getText().trim().isEmpty()) {
                        System.out.println("Message: " + msg.getText().trim());
                    }
                }
            } else {
                System.out.println("No explicit success/error messages found on screen.");
            }
            System.out.println("==================================================\n");
        } catch (Exception e) {
            System.out.println(">>> Could not extract specific result text, but flow completed.");
        }
    }
}
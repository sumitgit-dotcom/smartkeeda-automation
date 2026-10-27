package com.smartkeeda.steps;

import com.smartkeeda.hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SpeedDrillSteps {
	
	
	 private final WebDriver driver = Hooks.getDriver();
	    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    @When("I click on the {string} link in the header")
	    public void iClickOnTheLinkInTheHeader(String linkText) {
	        By locator = By.xpath("//a[normalize-space()='" + linkText + "']");
	        try {
	            WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
	            System.out.println(">>> Clicked header link: " + linkText);
	            Thread.sleep(3000); // Wait for page transition
	        } catch (Exception e) {
	            throw new RuntimeException("Could not find header link: " + linkText, e);
	        }
	    }
	    
	    @And("I click on the {string} link in the left pane")
	    public void iClickOnTheLinkInTheLeftPane(String linkText) {
	        By locator = By.xpath("//span[normalize-space()='" + linkText + "']");
	        try {
	            WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
	            Thread.sleep(500);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
	            System.out.println(">>> Clicked left pane link: " + linkText);
	            Thread.sleep(3000); // Wait for new tab to open
	        } catch (Exception e) {
	            throw new RuntimeException("Could not find left pane link: " + linkText, e);
	        }
	    }

	    
	    @And("I switch to the newly opened tab")
	    public void iSwitchToTheNewlyOpenedTab() {
	        // Get all open window handles
	        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
	        
	        if (windowHandles.size() > 1) {
	            // Switch to the last opened tab
	            driver.switchTo().window(windowHandles.get(windowHandles.size() - 1));
	            System.out.println(">>> Switched to new tab. Title: " + driver.getTitle());
	        } else {
	            System.out.println(">>> Warning: No new tab was opened. Staying on current tab.");
	        }
	        // Give the new tab a moment to fully render its React components
	        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
	    }
	    
	    @And("I click the {string} button on the drill card")
	    public void iClickTheButtonOnTheDrillCard(String buttonText) {
	        // Locate the specific button inside the solo-drill-card
	        By buttonLocator = By.xpath("//div[contains(@class,'solo-drill-card')]//span[contains(@class,'mdc-button__label')][normalize-space()='" + buttonText + "'] | //div[contains(@class,'drill-card')]//button[contains(., '" + buttonText + "')]");
	        
	        try {
	            WebElement startBtn = wait.until(ExpectedConditions.presenceOfElementLocated(buttonLocator));
	            
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", startBtn);
	            Thread.sleep(1000);
	            
	            
	            try {
	                startBtn.click(); // Try standard click first
	            } catch (Exception e) {
	                // If a Material UI overlay blocks it, force a JS click
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startBtn);
	            }
	            
	            System.out.println(">>> Clicked '" + buttonText + "' button.");
	            Thread.sleep(4000); // Wait for the drill to load
	        } catch (Exception e) {
	            throw new RuntimeException("Could not find or click the '" + buttonText + "' button.", e);
	        }
	    }
	    
	    
	    @Then("I print the drill page title")
	    public void iPrintTheDrillPageTitle() {
	        System.out.println("\n==================================================");
	        System.out.println("           SPEED DRILL STARTED                    ");
	        System.out.println("==================================================");
	        System.out.println("Current URL: " + driver.getCurrentUrl());
	        System.out.println("Page Title:  " + driver.getTitle());
	        System.out.println("==================================================\n");
	    }
	}





package myProject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
public class App {
    public static void main(String[] args) {
       String userEmail = getUserEmailFromInput();  // Ask user for email input
       String userPassword = getUserPasswordFromInput();  // Ask user for email input
       ArrayList<String> participants = new ArrayList<String>();
       participants = getParticipantsFromInput(); //Asks user for participants input from google sheets.
      int numofppl= participants.size();
       
    	// Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Configure ChromeOptions to bypass security warning
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Set desired capabilities
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        // Create a new instance of the ChromeDriver with desired capabilities
        WebDriver driver = new ChromeDriver(capabilities);

        // Open the webpage
        driver.get("https://www.remind.com");

        // Wait for the login button to be clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Approach 1: Locate login button by ID
        WebElement loginButton = driver.findElement(By.cssSelector("#main-nav > div.c-container.c-container-mobile-nav > div > div.c-nav_right > a"));
        loginButton.click();
        WebDriverWait slow = new WebDriverWait(driver, 10);
        WebElement loginWithGoogleButton = slow.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#app > div > div > div.MarketingLayout-content > main > div > div > div > div:nth-child(3) > button")));

        // Click the "Login with Google" button
        loginWithGoogleButton.click();
        
        String currentTab = driver.getWindowHandle();
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(currentTab)) {
                driver.switchTo().window(tab);
                break;
            }
        }

        // Input user's email in the field
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#identifierId")));
        emailField.sendKeys(userEmail);

        // Click the "Next" button
        WebElement nextButton = driver.findElement(By.cssSelector("#identifierNext > div > button > span"));
        nextButton.click();
        WebElement passwordfeild=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input")));
        passwordfeild.sendKeys(userPassword);
        WebElement nextButton2 = driver.findElement(By.cssSelector("#passwordNext > div > button > span"))	;
        nextButton2.click();
        // Click the science olympiad club.
        driver.switchTo().window(currentTab);

        
        WebElement sendmessage = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#app > div > div > div.AppSidebarLayout > div > div > div > div.AppSidebarLayout-sidebar > div.AccountNavigation > div.AccountNavigation-actions > div:nth-child(1) > button")));
        sendmessage.click();
        
        // search for participants
     // Switch to the popup window
        String mainWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        WebDriverWait popupWait = new WebDriverWait(driver, 10);

        for (int i=0; i<numofppl;i++)
        {
        	// Wait for elements inside the popup window to be visible
            WebElement searchBar = popupWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(26) > div > div > div > div > div > div > div > div > div > div > div.SelectRecipientPanel > div > div > div.SelectFromList-main > div.SearchBlock.SelectFromList-main-search > div > input")));

            
            // Interact with elements inside the popup window
            searchBar.sendKeys(participants.get(i));
            try {
                Thread.sleep(5000); // Wait for 2 seconds (2000 milliseconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebElement checkbox = popupWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("body > div:nth-child(26) > div > div > div > div > div > div > div > div > div > div > div.SelectRecipientPanel > div > div > div.SelectFromList-main > div.ScrollContainer > div > ul > li > div > div.MediaObject-figure > label > span > span.Checkbox-indicator")));

            checkbox.click();
            searchBar.clear();
        }
        
        

        WebElement continueButton = popupWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("body > div:nth-child(26) > div > div > div > div > div > div > div > div > div > div > div.SelectRecipientPanel > div > div > div.SelectFromList-sidebar > div.SelectFromList-sidebar-footer > button")));
        continueButton.click();

        // Switch back to the main window
        driver.switchTo().window(mainWindowHandle);
        
        // Now we will select the checkbox and proceed to send a message.
        // click select and send the message
        WebElement messagebar=wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#app > div > div > div.AppSidebarLayout > div > div > div > div.AppSidebarLayout-content > div > div.VisualComposer.ComposeMessage-composer-container > div > div > div.VisualComposer-composerContainer > div > div > textarea")));
        messagebar.sendKeys("Test message"); messagebar.sendKeys(Keys.ENTER);
        //WebElement sendButton = driver.findElement(By.cssSelector("#app > div > div > div.AppSidebarLayout > div > div > div > div.AppSidebarLayout-content > div > div.VisualComposer.ComposeMessage-composer-container > div > div > div.VisualComposerFooter.VisualComposer-buttonContainer > div.Buttons.Buttons--x.Buttons--reset--mobile---medium.Buttons--marginMedium > div:nth-child(2) > button"));
       // sendButton.click();

       
    }

    private static String getUserEmailFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your email: ");
        return ("Sidddharthpotta@gmail.com");
       // return scanner.nextLine();
    }
    private static String getUserPasswordFromInput() {

        Scanner monkey = new Scanner(System.in);
        System.out.print("Enter your password: ");
        return "Siddapro12345";
        
        //return monkey.nextLine();
    }
    public static ArrayList<String> getParticipantsFromInput() {
        ArrayList<String> participants = new ArrayList<String>();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the participant names (one name per line):");
        while (scanner.hasNextLine()) {
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                break; // Stop reading if an empty line is encountered
            }
            participants.add(name);
        }
        
        return participants;
    }
    
}

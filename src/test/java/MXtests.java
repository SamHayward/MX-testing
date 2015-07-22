import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MXtests {

    static WebDriver driver;
    static WebDriverWait wait;
    static WebDriverWait uploadWait;

    @BeforeClass
    public static void testingSetup(){
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver,15);
        uploadWait = new WebDriverWait(driver,10000);
    }

    @Before
    public void beforeEachTest(){
        driver.get("https://images.mymxdata.com");
    }

    @Test
    public void login(){

        WebElement emailInput = driver.findElement(By.cssSelector("input.emailAddress"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
        WebElement loginButton = driver.findElement(By.cssSelector("input.loginButton"));


        assertEquals("Check Title","Login", driver.getTitle());
        emailInput.sendKeys("matthew.trout@bespokesoftware.com");
        passwordInput.sendKeys("unhappyrocksleeps");
        loginButton.click();
        wait.until(titleIs("Select Host Company"));

        WebElement hostCompany = driver.findElement(By.cssSelector(".avatar-list-item a"));
        hostCompany.click();

        wait.until(titleIs("MX Landing Page"));


    }

    @Test
    public void sendUserInvite(){

        String inviteeEmail = (int)Math.floor(Math.random()*10000) + "@" + (int)Math.floor(Math.random()*10000) + ".com";

        login();

        WebElement cog = driver.findElement(By.cssSelector(".fui-gear"));
        WebElement inviteUsersOption = driver.findElement(By.cssSelector("[href='/faces/account/invite']"));
        cog.click();
        inviteUsersOption.click();
        wait.until(titleIs("Invite Users"));
        WebElement newUserEmail = driver.findElement(By.cssSelector("input.recipient-user-email"));
        newUserEmail.sendKeys(inviteeEmail);
        WebElement addButton = driver.findElement(By.cssSelector(".multipleUserInviter")).findElement(By.cssSelector("button"));
        addButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".submitUserInvitesBtn")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".submitUserInvitesBtn")));
        WebElement inviteUsersButton = driver.findElement(By.cssSelector(".submitUserInvitesBtn"));
        inviteUsersButton.click();

        WebElement existingInvitesForm = driver.findElement(By.id("existingInvitesForm"));
        WebElement newlyInvitedEmail = existingInvitesForm.findElement(By.cssSelector("tbody tr:first-child td:first-child"));
        assertEquals("Check user was added to invited users list",inviteeEmail, newlyInvitedEmail.getText());
        System.out.println("Checking email that was invited: (" + inviteeEmail + "), matches email in the existing invites form: (" + newlyInvitedEmail.getText() + ")");

    }

    @Test
    public void uploadAndSendData(){

        login();

        JavascriptExecutor js = (JavascriptExecutor)driver;

        WebElement sendDataOption = driver.findElement(By.cssSelector(".arrowButton.up"));
        sendDataOption.click();
        wait.until(titleIs("Send Files"));

        WebElement internalGroupTab = driver.findElement(By.cssSelector(".user-tab.internal"));
        internalGroupTab.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".lozenge")));

        WebElement aLozenge = driver.findElement(By.cssSelector(".lozenge"));
        WebElement addRecipientsButton = driver.findElement(By.cssSelector("#addRecipientButton"));

        //add recipient
        aLozenge.click();
        addRecipientsButton.click();

        //upload files
        js.executeScript("document.getElementById('fileUploadInput').style.display = 'block'");
        WebElement uploadData = driver.findElement(By.cssSelector("#fileUploadInput"));
        uploadData.sendKeys("C:\\Users\\samh\\Documents\\Majenta\\test-file.txt");

        //send files
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".send-files-button")));
        WebElement sendFilesButton = driver.findElement(By.cssSelector(".send-files-button"));
        sendFilesButton.click();
        wait.until(titleIs("MX Landing Page"));

    }

    @AfterClass
    public static void testingTearDown(){
        driver.quit();
    }


}



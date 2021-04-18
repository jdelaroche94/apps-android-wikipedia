package org.wikipedia.test;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class LoginInvalidCredentialsTest {

    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "LoginUITest";
    protected AndroidDriver<AndroidElement> driver = null;

    DesiredCapabilities dc = new DesiredCapabilities();


    @BeforeMethod
    public void setUp() throws MalformedURLException {
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability(MobileCapabilityType.UDID, "ce12160c6ad9eb0d01");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "org.wikipedia.alpha");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "org.wikipedia.main.MainActivity");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), dc);
        driver.setLogLevel(Level.INFO);
    }



    @DataProvider (name = "data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {
                {"Invalid Password","TestingWiki1234","InvalidCredentials", true},
                {"Invalid Username","TestingUser","Test@123",true},
                {"Invalid Username and Password","TestingUser","InvalidCredentials", true},
        };
    }

    /**
     Scenario: Login into the Wiki using invalid username or password
     Given I have entered a valid/invalid username
     And I have entered a valid/invalid password
     When I press login
     Then I should receive an error requesting entering my credentials again
     */
    @Test(dataProvider = "data-provider")
    public void LoginWithInvalidCredentials(String caseStudy, String username, String password, boolean expectedResult) {
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN / JOIN WIKIPEDIA']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        //Given I have entered a valid/invalid username
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@id='textinput_placeholder']]")).sendKeys(username);
        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")));
        //And I have entered a valid/invalid password
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")).sendKeys(password);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='LOG IN']")));
        //When I press login
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.FrameLayout' and ./*[@class='android.widget.LinearLayout' and ./*[@id='snackbar_text']]]")));
        driver.findElement(By.xpath("//*[@class='android.widget.FrameLayout' and ./*[@class='android.widget.LinearLayout' and ./*[@id='snackbar_text']]]")).click();
        String resultText = driver.findElement(By.xpath("//*[@text='Incorrect username or password entered.\\nPlease try again.']")).getText();
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        Reporter.log("Scenario: Login into the Wiki using "+caseStudy+"\n" +
                "        Given I have entered a valid/invalid username called "+username+"\n" +
                "        And I have entered a valid/invalid password called " +password+ "\n" +
                "        When I press login\n" +
                "        Then I should receive an error message",true);
        Reporter.log("Evidence Picture available at " + screenShotFile.getAbsolutePath(),true);
        boolean actualResult = resultText.contains("Incorrect username or password entered");
        Assert.assertEquals(actualResult, expectedResult);
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

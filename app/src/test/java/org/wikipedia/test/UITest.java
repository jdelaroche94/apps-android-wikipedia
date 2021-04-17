package org.wikipedia.test;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.Reporter;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.io.File;

//@RunWith(Parameterized.class)
public class UITest {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "UITest";
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

    //This test method allows to validate Login into the application using valid credentials
    @Test
    public void WhenIAddValidCredentials() throws InterruptedException {
        String expectedResult = "TestingWiki1234";
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN / JOIN WIKIPEDIA']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@id='textinput_placeholder']]")).sendKeys("testingWiki1234");
        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")));
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")).sendKeys("Test@123");
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='LOG IN']")));
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='menu_icon']")));
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        String actualResult = driver.findElement(By.xpath("//*[@id='main_drawer_account_name']")).getText();
        File screenshotFile1 = driver.getScreenshotAs(OutputType.FILE);
        TimeUnit.SECONDS.sleep(6);
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        Reporter.log("This test verifies the login with valid credentials in the Wiki App - Android Version",true);
        Reporter.log("Evidence Picture available at " + screenshotFile1.getAbsolutePath(),true);
        Assert.assertEquals(actualResult,expectedResult);
    }

    //This test method allows to validate Login capitalizing a letter of the username
    @Test
    public void WhenThereIsACredentialWithACapitalLetterMismatch() throws InterruptedException {
        String expectedResult = "testingWiki1234";
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN / JOIN WIKIPEDIA']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@id='textinput_placeholder']]")).sendKeys("testingWiki1234");
        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")));
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")).sendKeys("Test@123");
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='LOG IN']")));
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='menu_icon']")));
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        String actualResult = driver.findElement(By.xpath("//*[@id='main_drawer_account_name']")).getText();
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        TimeUnit.SECONDS.sleep(6);
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        Reporter.log("This test verifies a login capitalizing a letter of a valid credentials in the Wiki App - Android Version",true);
        Reporter.log("Evidence Picture available at " + screenShotFile.getAbsolutePath(),true);
        Assert.assertEquals(actualResult,expectedResult);

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
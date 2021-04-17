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

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class LoginParam {

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


    @DataProvider (name = "data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {
                {"testingWiki1234","Test@123"},
                {"TestingWiki1234","Test@123"}
        };
    }


    @Test (dataProvider = "data-provider")
    public void validCredentialsParam(String username, String password) throws InterruptedException {
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN / JOIN WIKIPEDIA']")).click();
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@id='textinput_placeholder']]")).sendKeys(username);
        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")));
        driver.findElement(By.xpath("//*[@class='android.widget.EditText' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.widget.LinearLayout']]")).sendKeys(password);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='LOG IN']")));
        driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='menu_icon']")));
        driver.findElement(By.xpath("//*[@id='menu_icon']")).click();
        String actualResult = driver.findElement(By.xpath("//*[@id='main_drawer_account_name']")).getText();
        File screenshotFile1 = driver.getScreenshotAs(OutputType.FILE);
        TimeUnit.SECONDS.sleep(4);
        //new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='menu_icon']")));
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        driver.findElement(By.xpath("//*[@text='LOG OUT']")).click();
        Reporter.log("This test verifies the login with valid credentials in the Wiki App - Android Version",true);
        Reporter.log("This is an absolute path " + screenshotFile1.getAbsolutePath(),true);
        Assert.assertEquals(actualResult, username);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

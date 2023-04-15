package edu.chnu;
/*
  @author   george
  @project   tdd-fe-test
  @class  TestingViaSelenium
  @version  1.0.0 
  @since 04.04.23 - 18.52
*/

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class TestingMacBookPrice {

    private static final String BASE_URL = "http://taqc-opencart.epizy.com/";
    private static final Long  IMPLICITY_WAIT_SECONDS = 10L;
    private static final Long  ONE_SECOND_DELAY = 1000L;
    private static final int DELAY = 2;
    private WebDriver driver;

    private void delayDemo(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeSuite
    public void beforeSuite(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeClass
    public void beforeClass(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITY_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass(){
        delayDemo(DELAY);
        if (driver != null){
            driver.quit();
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get(BASE_URL);
        delayDemo(DELAY);
    }

    @AfterMethod
    public void afterMethod(ITestResult result){
        delayDemo(DELAY);

        if (!result.isSuccess()) {
            System.out.println(" ERROR, name = " + result.getName());
           createScreenShot();
           createPageSource();
        }
    }

    private void createScreenShot(){
        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(screenShot, new File("./screenshots/" + LocalDateTime.now() + "_screen.png"));}
        catch (IOException e) {
            throw new RuntimeException(e);
            }
    }

    private void createPageSource(){
        String pageSource = driver.getPageSource();
        byte[] pageSourceInBytes = pageSource.getBytes();
        Path path = Paths.get("./sources/" + LocalDateTime.now() +"_src.html");
        try{
            Files.createDirectories(Paths.get("./sources/"));
            Files.write(path, pageSourceInBytes, StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
}
    }

    @Test
    public void searchElementsByCss(){
        driver.findElement(By.cssSelector("button.btn.btn-link.dropdown-toggle")).click();
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("button[name='USD']")).click();
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("#search > input")).click();
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("#search > input")).clear();
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("#search > input")).sendKeys("mac");
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("button.btn.btn-default.btn-lg")).click();
        delayDemo(DELAY);
        List<WebElement> containers = driver.findElements(By.cssSelector("div.product-layout.product-grid"));
        WebElement container = containers.get(1);
        WebElement we = containers.stream()
                .filter(el -> el.findElement(By.cssSelector("h4 > a")).getText().equals("MacBook"))
                .findAny().orElseThrow();


        WebElement element = container.findElement(By.cssSelector("p.price"));
        Actions action = new Actions(driver);
        action.moveToElement(element);
        delayDemo(8);
        Assert.assertTrue(element.getText().contains("$602.00"));

    }


}

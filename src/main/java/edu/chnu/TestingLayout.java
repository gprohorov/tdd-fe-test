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
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.time.Duration;
import java.time.LocalDateTime;


public class TestingLayout {

    private static final String BASE_URL = "http://taqc-opencart.epizy.com/";
    private static final Long  IMPLICITY_WAIT_SECONDS = 10L;
    private static final Long  ONE_SECOND_DELAY = 1000L;
    private static final int DELAY = 1;
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
        createPageSource();
        if (!result.isSuccess()) {
            System.out.println(" ERROR, name = " + result.getName());
         //   createScreenShot();
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
    public void exploreSelenium(){
        driver.findElement(By.cssSelector("a[title='My Account']")).click();
        delayDemo(DELAY);
        driver.findElement(By.linkText("Register")).click();
        delayDemo(DELAY);
        driver.findElement(By.name("firstname")).sendKeys("Freddie", Keys.TAB,"Mercury", Keys.ENTER);
        delayDemo(5);

    }


}

package edu.chnu;
/*
  @author   george
  @project   tdd-fe-test
  @class  TestingViaSelenium
  @version  1.0.0 
  @since 04.04.23 - 18.52
*/

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestingViaSelenium {

    private static final String BASE_URL = "http://taqc-opencart.epizy.com/";
    private static final Long  IMPLICITY_WAIT_SECONDS = 10L;
    private static final Long  ONE_SECOND_DELAY = 1000L;
    private static final int DELAY = 1;

    private WebDriver driver = new ChromeDriver();

    private void delayDemo(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void exploreSelenium(){

        WebDriverManager.chromedriver().setup();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITY_WAIT_SECONDS));
        driver.manage().window().maximize();
        delayDemo(DELAY);
        driver.get(BASE_URL);
        delayDemo(DELAY);
        driver.findElement(By.cssSelector("a[title='My Account']")).click();
        delayDemo(DELAY);
        driver.findElement(By.linkText("Register")).click();
        delayDemo(DELAY);
        driver.findElement(By.name("firstname")).sendKeys("Freddie", Keys.TAB,"Mercury", Keys.ENTER);
        delayDemo(5);

        driver.quit();
    }


}

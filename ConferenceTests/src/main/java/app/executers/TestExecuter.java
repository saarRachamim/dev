package app.executers;

import app.common.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class TestExecuter {

    /**
     * The function executes an infint test
     */
    public void execute(){
        System.setProperty("webdriver.chrome.driver", "path_to_chrome_driver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        boolean stop = true;

        while(stop){
            try{
                driver.get(Constants.GOOGLE);
                WebElement searchField = driver.findElement(By.xpath(Constants.SEARCH_FIELD));
                searchField.sendKeys("String to search at the search field");
                WebElement searchButton = driver.findElement(By.xpath(Constants.SEARCH_BUTTON));
                searchButton.submit();
                Thread.sleep(Constants.FIVE_SECONDS);
            }
            catch(Exception e){
                System.out.print(e.getMessage());
            }
        }
    }
}

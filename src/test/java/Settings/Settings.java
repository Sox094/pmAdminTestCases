package Settings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Settings {

    public void setupSettings() {
        //WebDriver associated with chrome
        WebDriver browser;
        browser = new ChromeDriver();

        String baseUrl = "http://localhost:8080/";

        //open chrome at the current url baseUrl
        browser.get(baseUrl);

        //maximize the window
        browser.manage().window().maximize();

        //retrieve h3 title
        String title = "";
        title = browser.findElement(By.tagName("h3")).getText();
        System.out.println(title);

    }

    //@Test
    public void SettingsTest() {

    }
}

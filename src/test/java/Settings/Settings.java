package Settings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import Helper.Browser;
import org.testng.annotations.Test;

public class Settings extends Browser{

    @Test
    public void testSettings() {
        initialization();
        precondition();
        assertion1();
        waitFor(3000);
        closeBrowser();
    }

    private void assertion1() {
    }

    private void precondition() {
        //go to the settings tab
        openSettingsPage();
    }

    private void initialization() {
        setUp();
    }
}

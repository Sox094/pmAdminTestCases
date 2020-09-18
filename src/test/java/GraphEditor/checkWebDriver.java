package GraphEditor;

import static org.junit.Assert.*;

import com.sun.javafx.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.vaadin.gatanaso.MultiselectComboBox;

public class checkWebDriver {

    public WebDriver openBrowser(){
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

        return browser;
        //close chrome
        //browser.close();
    }

    @Test
    public void GraphEditorTest() {
        WebDriver browser;
        browser = openBrowser();

        //find button add Node
        browser.findElement(By.id("addNode")).click();

        //adapt to vaadin timeout
        WebDriverWait wait = new WebDriverWait(browser, 100);

        WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.id("addNode_name")));
        WebElement type = browser.findElement(By.id("addNode_type"));
        WebElement parent = wait.until(ExpectedConditions.elementToBeClickable(By.id("addNode_parent")));

        //Select selectParent = new Select(parent);

        //complete input
        name.sendKeys("new node");
        type.sendKeys("U");

        WebElement dropdown = browser.findElement(By.name("addNode_parent"));

        Select select = new Select(dropdown);
        select.selectByValue("super_ua2");
        //selectTheDropDownList(parent, "super_ua2");
        //selectTheDropDownList(parent, "super_ua1");

        browser.findElement(By.id("addNode_submit")).click();

    }

    public static void selectTheDropDownList(WebElement dropDown,String text) {
        Select select = new Select(dropDown);
        select.selectByVisibleText(text);
    }

    //discuss with the team about all the assertions
    private void assertion1() {}

    private void assertion2() {}

}

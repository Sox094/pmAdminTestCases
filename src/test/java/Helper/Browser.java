package Helper;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.Locale;

public abstract class Browser {
    protected WebDriver driver;

    private static OperatingSystem getOperatingSystem() {
        String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (osName.contains("mac") || osName.contains("darwin")) {
            return OperatingSystem.MacOSX;
        } else if (osName.contains("win")) {
            return OperatingSystem.Windows;
        } else if (osName.contains("nux")) {
            return OperatingSystem.Linux;
        } else {
            return OperatingSystem.Other;
        }
    }

    private static String getWebdriverPath() {
        String webdriver;
        switch (getOperatingSystem()) {
            case Windows:
                webdriver = "webdriver/chromedriver.exe";
                break;
            case MacOSX:
                webdriver = "webdriver/chromedriver_mac64";
                break;
            case Linux:
                webdriver = "webdriver/chromedriver_linux64";
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Operating System: " + (System.getProperty("os.name")));
        }
        ClassLoader classLoader = Browser.class.getClassLoader();
        URL webdriverResource = classLoader.getResource(webdriver);
        if (webdriverResource == null) {
            throw new IllegalStateException("Can't find webdriver '" + webdriver + "' from resources (root path: " + classLoader.getResource(".") + ").");
        }
        return webdriverResource.getPath();
    }

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", getWebdriverPath());
        //headless mode
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--disable-notifications");
//        chromeOptions.addArguments("--mute-audio");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080");
        //add implicitly wait
//        driver.manage().timeouts().implicitlyWait(1250, TimeUnit.MILLISECONDS);
        getDriver().manage().window().maximize();
    }

    protected WebDriver getDriver() {
        return driver;
    }

    public void closeBrowser() {
        driver.close();
    }

    private enum OperatingSystem {
        Windows,
        MacOSX,
        Linux,
        Other
    }


    //hereafter are some methods made to simplify the use of selenium

    //use method clickOn if you do not want to interact with the WebElement (sendKeys, etc...)
    protected void clickOn(WebElement element) {
        try {
            Actions action = new Actions(getDriver());
            action.moveToElement(element).perform();
            element.sendKeys(Keys.ENTER);
        } catch (Exception rerun) {
            waitFor(2000);
            String ele = element.toString();
            String ppath;
            //issue: sometime selenium adds a bracket ] at the end. Remove it.
            if (ele.contains("]]")) {
                ppath = ele.substring(ele.indexOf(" //"), ele.indexOf("]]") + 1);
            } else if (ele.contains("span]")) {
                ppath = ele.substring(ele.indexOf(" //"), ele.indexOf("span]") + 4);
            } else if (ele.contains("div]")) {
                ppath = ele.substring(ele.indexOf(" //"), ele.indexOf("div]") + 3);
            } else if (ele.contains("a]")) {
                ppath = ele.substring(ele.indexOf(" //"), ele.indexOf("a]") + 1);
            } else if (ele.contains("icon]")) {
                ppath = ele.substring(ele.indexOf(" //"), ele.indexOf("icon]") + 4);
            } else {
                ppath = ele.substring(ele.indexOf(" //"));
            }
            WebElement recreatedElement = getDriver().findElement(By.xpath(ppath));
            Actions action = new Actions(getDriver());
            action.moveToElement(recreatedElement).perform();
            recreatedElement.click();
        }
    }

    protected void waitForElementToBecomePresent(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 35);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected boolean isElementPresent(By by) {
        try {
            getDriver().findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void waitForElementToBecomeClickable(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 15);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void waitForElementToBecomeClickable(WebElement anElement) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 15);
        wait.until(ExpectedConditions.elementToBeClickable(anElement));
    }

    protected WebElement getElementByXPath(String anXpath) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 155);
        waitForPageLoaded();
        waitFor(400);
        try {
            waitForElementToBecomePresent(By.xpath(anXpath));
            //highlight elements
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", getDriver().findElement(By.xpath(anXpath)), "color: red; border: 2px solid red;");
        } catch (StaleElementReferenceException sere) {
            System.out.println("stale exception prevented");
        }
        return getDriver().findElement(By.xpath(anXpath));
    }

    protected void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(500);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    protected WebElement getElementByXPathFast(String anXpath) {
        waitForElementToBecomePresent(By.xpath(anXpath));
        return getDriver().findElement(By.xpath(anXpath));
    }

    protected void verifyElementIsNotPresent(By by) {
        Assert.assertTrue(!isElementPresent(by));
    }

    protected void openSettingsPage() {
        WebElement settings_tab = getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[9]"));
        clickOn(settings_tab);
    }

    protected void pressEscape() {
        Actions action = new Actions(getDriver());
        action.sendKeys(Keys.ESCAPE).build().perform();
    }

    protected void pressDown() {
        Actions action = new Actions(getDriver());
        action.sendKeys(Keys.DOWN).build().perform();
    }

    protected void pressEnter() {
        Actions action = new Actions(getDriver());
        action.sendKeys(Keys.ENTER).build().perform();
    }

    protected void pressPageDown() {
        new Actions(getDriver()).sendKeys(Keys.PAGE_DOWN).perform();
    }

    protected void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected WebElement getAddNode() {
        return getDriver().findElement(By.xpath("//*[@id=\"addNode\"]"));
    }

    protected WebElement getAddUser() {
        return getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/div/graph-editor/vaadin-horizontal-layout/vaadin-vertical-layout[3]/vaadin-button[2]"));
    }

    protected WebElement getAddObject() {
        return getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/div/graph-editor/vaadin-horizontal-layout/vaadin-vertical-layout[3]/vaadin-button[3]"));
    }

    protected WebElement getSuper_pc() {
        return getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/div/graph-editor/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-grid/vaadin-grid-cell-content[1]"));
    }

    protected WebElement getChevron(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String chevron = "document.querySelector(\"body > vaadin-horizontal-layout > div > graph-editor > vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > vaadin-grid > vaadin-grid-cell-content:nth-child(3) > vaadin-grid-tree-toggle\").shadowRoot.querySelector(\"span:nth-child(3)\").click()";
        return (WebElement) js.executeScript(chevron);
    }

    protected WebElement openFirstChevron() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String chevron = "document.querySelector(\"body > vaadin-horizontal-layout > div > graph-editor > vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > vaadin-grid > vaadin-grid-cell-content:nth-child(3) > vaadin-grid-tree-toggle\").shadowRoot.querySelector(\"span:nth-child(3)\").click()";
        return (WebElement) js.executeScript(chevron);
    }

    protected void doubleClick(WebElement element) {
        Actions actions = new Actions(getDriver());
        actions.doubleClick(element).perform();
    }

    public void getDropdownValue(WebElement webElement) {
        Select select = new Select(webElement);
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            System.out.println(option.getText());
        }
    }

}

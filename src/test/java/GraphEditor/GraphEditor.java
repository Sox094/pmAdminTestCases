package GraphEditor;

import static org.junit.Assert.*;

import Helper.Browser;
import com.sun.javafx.util.Utils;
import entity.Node;
import entity.NodeType;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphEditor extends Browser {

    @Test
    public void testGraphEditor() {
        initialization();
        precondition();
        //assertion1();
        //assertion3();
        closeBrowser();
    }

    private void initialization() {
        //setup driver
        setUp();
    }

    private void precondition() {
        //if we need to test some uniqueness within the graph we can create nodes here
        HashMap<String, String> properties = new HashMap<>();
        properties.put("test input1", "value 1");
        properties.put("test input2", "value 2");
        properties.put("test input3", "value 3");
        Node node_to_add = new Node("new node test precond", NodeType.UA, properties);
        addNode(node_to_add, "super_ua2", "super_ua1");
        //addUser(node_to_add, "super_ua2", "super_ua1");
        //addObject(node_to_add, "super_oa", "super_pc_rep");
    }

    public static void selectTheDropDownList(WebElement dropDown,String text) {
        Select select = new Select(dropDown);
        select.selectByVisibleText(text);
    }

    private void assertion1() {
        String title = "";
        title = getDriver().findElement(By.tagName("h3")).getText();
        System.out.println(title);
    }

    /*
    interact with buttons
     */
    private void addNode(Node node, String... parents) {
        //adapt to vaadin timeout
        WebDriverWait wait = new WebDriverWait(getDriver(), 100);

        WebElement button_add_node = getAddNode();
        clickOn(button_add_node);

        WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.id("addNode_name")));
        WebElement type = getDriver().findElement(By.id("addNode_type"));

        //complete input
        name.sendKeys(node.getName());
        type.sendKeys(node.getType().toString());

        // select parents
        //wait to make sure we have already completed the input
        waitFor(2000);
        //find parent element and focus onto it
        WebElement parent_element = getDriver().findElement(By.id("addNode_parent"));
        parent_element.click();

        //open dropdown using JS - no need to do that since the dropdown open when on focus to the component
        /*JavascriptExecutor js = (JavascriptExecutor) driver;
        String openDropdown = "document.querySelector(\"#addNode_parent\").shadowRoot.querySelector(\"#input\").shadowRoot.querySelector(\"#inputField\").shadowRoot.querySelector(\"#vaadin-text-field-input-4 > slot:nth-child(2) > input\").click()";
        js.executeScript(openDropdown);*/

        waitFor(2000);
        Actions actions = new Actions(getDriver());
        for (String parent: parents) {
            actions.moveToElement(parent_element).sendKeys(parent).perform();
            pressEnter();
        }

        WebElement properties = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/vaadin-horizontal-layout/div[2]"));
        int cnt = 0;
        HashMap<WebElement, WebElement> properties_input = new HashMap<>();
        for (Map.Entry<String, String> property : node.getProperties().entrySet()) {
            clickOn(properties);
            int i = cnt +1;
            /*clickOn(properties);
            WebElement remove_properties1 = (WebElement) js.executeScript("document.querySelector(\"#overlay > flow-component-renderer > div > vaadin-horizontal-layout:nth-child(3) > vaadin-vertical-layout > div > vaadin-horizontal-layout:nth-child(2) > vaadin-button\").shadowRoot.querySelector(\"#button\")");
            clickOn(remove_properties1);*/
            WebElement properties_input1;
            WebElement properties_input2;
            if (cnt == 0) {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[2]")));

            } else {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[2]")));

            }
            waitFor(1000);
            properties_input1.sendKeys(property.getKey());
            properties_input2.sendKeys(property.getValue());
            properties_input.put(properties_input1, properties_input2);
            cnt++;
        }
        WebElement button_submit = getDriver().findElement(By.id("addNode_submit"));
        clickOn(button_submit);

        //wait for the creation of the node
        waitFor(3000);
    }

    private void addUser(Node node, String... parents) {

        WebDriverWait wait = new WebDriverWait(getDriver(), 100);

        WebElement addUser_button = getAddUser();
        clickOn(addUser_button);
        waitFor(2000);

        WebElement name = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-text-field"));
        name.sendKeys(node.getName());

        waitFor(2000);
        WebElement parent_element = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/multiselect-combo-box"));
        parent_element.click();
        waitFor(2000);
        Actions actions = new Actions(getDriver());
        for (String parent: parents) {
            actions.moveToElement(parent_element).sendKeys(parent).perform();
            pressEnter();
        }

        WebElement properties = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/vaadin-horizontal-layout/div[2]"));
        int cnt = 0;
        HashMap<WebElement, WebElement> properties_input = new HashMap<>();
        for (Map.Entry<String, String> property : node.getProperties().entrySet()) {
            clickOn(properties);
            int i = cnt +1;
            WebElement properties_input1;
            WebElement properties_input2;

            if (cnt == 0) {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[2]")));
            } else {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[2]")));
            }

            waitFor(1000);
            properties_input1.sendKeys(property.getKey());
            properties_input2.sendKeys(property.getValue());
            properties_input.put(properties_input1, properties_input2);
            cnt++;
        }

        WebElement addUser_submit = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[1]/vaadin-vertical-layout[2]/vaadin-button"));
        clickOn(addUser_submit);
        waitFor(3000);
    }

    private void addObject(Node node, String... parents) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 100);

        WebElement addObject_button = getAddObject();
        clickOn(addObject_button);
        waitFor(2000);

        WebElement name = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-text-field"));
        name.sendKeys(node.getName());

        WebElement parent_element = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/multiselect-combo-box"));
        parent_element.click();
        waitFor(2000);
        Actions actions = new Actions(getDriver());
        for (String parent: parents) {
            actions.moveToElement(parent_element).sendKeys(parent).perform();
            pressEnter();
        }

        WebElement properties = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/vaadin-horizontal-layout/div[2]"));
        int cnt = 0;
        HashMap<WebElement, WebElement> properties_input = new HashMap<>();
        for (Map.Entry<String, String> property : node.getProperties().entrySet()) {
            clickOn(properties);
            int i = cnt +1;
            WebElement properties_input1;
            WebElement properties_input2;

            if (cnt == 0) {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout/vaadin-text-field[2]")));
            } else {
                properties_input1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[1]")));
                properties_input2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[2]/vaadin-vertical-layout/div/vaadin-horizontal-layout[" + i + "]/vaadin-text-field[2]")));
            }

            waitFor(1000);
            properties_input1.sendKeys(property.getKey());
            properties_input2.sendKeys(property.getValue());
            properties_input.put(properties_input1, properties_input2);
            cnt++;
        }

        WebElement addObject_submit = getDriver().findElement(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-horizontal-layout[1]/vaadin-vertical-layout[2]/vaadin-button"));
        clickOn(addObject_submit);

        waitFor(2000);
    }



    //interact with the grid
    private void assertion3() {

        WebElement super_pc = getSuper_pc();
        super_pc.click();

        doubleClick(super_pc);

        /*WebElement super_oa = openFirstChevron();
        super_oa.click();


        WebElement node1 = driver.findElement(By.xpath("/html/body/vaadin-horizontal-layout/div/graph-editor/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-grid/vaadin-grid-cell-content[11]"));
        actions.doubleClick(node1).perform();*/
        waitFor(2000);


        /*WebElement edit_node_js = null;
        edit_node_js = (WebElement) ((JavascriptExecutor) getDriver()).executeScript("document.querySelector(\"#overlay > vaadin-context-menu-list-box > vaadin-context-menu-item:nth-child(1)\")");
        actions.contextClick(super_pc).perform();
        edit_node_js.click();*/



        //WebElement chevron = chevron3();
        //chevron.click();
        //doubleClick(super_pc);
    }

}

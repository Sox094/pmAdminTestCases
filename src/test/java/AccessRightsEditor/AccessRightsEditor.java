package AccessRightsEditor;

import Helper.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class AccessRightsEditor extends Browser {

    @Test
    public void AccessRightsEditor() {
        initialization();
        precondition();
        assertion1();
        assertion2();
    }

    private void assertion2() {
    }

    private void assertion1() {
    }

    private void precondition() {
        //Go to the Access rights tab
        WebElement settings_tab = getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[7]"));
        clickOn(settings_tab);
    }

    private void initialization() {
        setUp();
    }
}

package ImportExport;

import Helper.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ImportExportTestCases extends Browser {
    
    @Test
    public void ImportExport(){
        initialization();
        precondition();
        exportJson();
        assertion1();
        assertion2();
        waitFor(3000);
        closeBrowser();
    }

    private void precondition() {
        //Go to the importExport tab
        WebElement settings_tab = getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[3]"));
        clickOn(settings_tab);
    }

    private void initialization() {
        setUp();
    }

    private void assertion2() {
    }

    private void assertion1() {
    }

    private void exportJson() {
        WebElement exportButton = getDriver().findElement(By.xpath("/html/body/vaadin-horizontal-layout/div/import-export/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-button"));
        clickOn(exportButton);
    }
}

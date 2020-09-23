package page_objects_test;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page_objects.HomePage;
import webBrowsers.BrowserDriver;

public class HomePageTest extends BrowserDriver {

    HomePage homePage = null;

    @BeforeMethod
    public void initializeElements(){
        homePage = PageFactory.initElements(driver, HomePage.class);
    }

    @Test
    public void searchBoxTest() throws InterruptedException {
        homePage.searchBoxText();
        //Thread.sleep(9000);
    }

    @Test
    public void womenDeptButtonTest(){
        homePage.womenDeptButtonClick();

    }

    @Test
    public void menDeptButtonTest(){
        homePage.menDeptButtonClickTest();
    }

}

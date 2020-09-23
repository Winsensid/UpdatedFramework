package page_objects_test;

import webBrowsers.BrowserDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page_objects.MyCart;

public class MyCartTest extends BrowserDriver {

    MyCart myCart = null;

    @BeforeMethod
    public void initializeElements(){
        myCart = PageFactory.initElements(driver, MyCart.class);
    }

    @Test
    public void myCartButtonTest()  {
        myCart.myCartButtonClick();

    }

}

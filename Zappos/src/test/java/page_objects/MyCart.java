package page_objects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class MyCart {


    @FindBy(how = How.CLASS_NAME, using = "o")
    WebElement myCartButton;
    @FindBy(how = How.ID, using = "modalDescription")
    WebElement myCartText;

    public void myCartButtonClick(){
        myCartButton.click();
        String confirmText = myCartText.getText();
        Assert.assertEquals(confirmText, "My Cart");
        //System.out.println(confirmText);


    }
}

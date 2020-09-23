package page_objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class HomePage {

    WebDriver driver = null;

    @FindBy(how = How.ID, using = "searchAll")
    WebElement searchBox;

    @FindBy(how = How.XPATH, using = "//button[@type='submit]")
    WebElement searchButton;

    @FindBy(how = How.CLASS_NAME, using = "RS")
    WebElement confimationMessage;


    public void searchBoxText() throws InterruptedException {
        searchBox.sendKeys("Converse All Star");
        searchButton.click();
        String confirmMessage = confimationMessage.getText();
        Assert.assertEquals(confirmMessage,"258 items found");
        //System.out.println(confirmMessage);
        Thread.sleep(9000);




    }

    @FindBy(how = How.XPATH, using = "//a[@data-shyguy='navWomen']")
    WebElement womenDeptButton;

    @FindBy(how = How.XPATH, using = "//a[@data-hfsubnav='navWomen=Shoes']")
    WebElement shoesText;

    public void womenDeptButtonClick(){
        womenDeptButton.click();
        String shoesTextConfirm = shoesText.getText();
        Assert.assertEquals(shoesTextConfirm, "Shoes");
        System.out.println(shoesTextConfirm);
    }

    @FindBy(how = How.XPATH, using = "//a[@data-shyguy='navMen']")
    WebElement menDeptButtonClick;
    @FindBy(how = How.XPATH, using = "//a[@data-hfsubnav='navMen=Clothing']")
    WebElement clothingText;

    public void menDeptButtonClickTest(){
        menDeptButtonClick.click();
        String clothingTextConfirmation = clothingText.getText();
        Assert.assertEquals(clothingTextConfirmation,"Clothing");


    }


    }


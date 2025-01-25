package com.RapifuzzPvt.Ltd.Assignment.Page_Or_Layer;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.RapifuzzPvt.Ltd.Assignment.Util_Layer.WebUtil;

import lombok.Getter; // Make sure Lombok is working
@Getter
public class RapifuzzOR
{
	WebUtil gm=WebUtil.getInstance();

    public RapifuzzOR(WebUtil gm) {
        this.gm = gm;
        PageFactory.initElements(gm.driver, this);
    }

    @FindBy(xpath = "//input[@id='txtStationFrom']")
    private WebElement FromINPUT;

    @FindBy(xpath = "//div[@title='Delhi Azadpur']//div[1]")
    private List<WebElement> SelectDP;

    @FindBy(xpath="//input[@id='txtStationTo']")
    private WebElement UsertxtStationTo;
    
    @FindBy(xpath="//input[@title='Select Departure date for availability']")
    private WebElement UserDepartureUpdate;
    
    @FindBy(xpath = "//input[@value=\"29-Dec-24 Sun\"]")
    private List<WebElement>  dateUse;
    
    @FindBy(xpath="//table[@class='Month']")
    private List<WebElement> UserAllMonth;
  
}

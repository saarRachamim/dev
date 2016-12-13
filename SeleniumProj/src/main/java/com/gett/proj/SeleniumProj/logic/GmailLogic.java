package com.gett.proj.SeleniumProj.logic;

import org.openqa.selenium.WebDriver;

import com.gett.proj.SeleniumProj.enums.ByMethod;
import com.gett.proj.SeleniumProj.helpers.FindElementHelper;

public class GmailLogic extends LogicalObject {

	public GmailLogic(FindElementHelper findElementHelper, WebDriver driver) {
		super(findElementHelper, driver);
	}

	public void execute() {
		findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@type='email']").sendKeys("saarrac@gmail.com");;
		findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@id='next']").click();
	}

}

package com.gett.proj.SeleniumProj.logic;

import org.openqa.selenium.WebDriver;

import com.gett.proj.SeleniumProj.enums.ByMethod;
import com.gett.proj.SeleniumProj.helpers.FindElementHelper;
import com.gett.proj.SeleniumProj.interfaces.AppConstants;

public class FacebookLogic extends LogicalObject {

	public FacebookLogic(FindElementHelper findElementHelper, WebDriver driver) {
		super(findElementHelper, driver);
	}

	public void execute() {
		try {
			findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@type='email']").sendKeys("danx@walla.co.il");
			Thread.sleep(AppConstants.FIVE_SEC);
			findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@id='pass']").sendKeys("57943036");	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}

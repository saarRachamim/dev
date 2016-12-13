package com.gett.proj.SeleniumProj.logic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gett.proj.SeleniumProj.enums.ByMethod;
import com.gett.proj.SeleniumProj.helpers.FindElementHelper;
import com.gett.proj.SeleniumProj.interfaces.AppConstants;

public class GoogleLogic extends LogicalObject {

	public GoogleLogic(FindElementHelper findElementHelper, WebDriver driver) {
		super(findElementHelper, driver);
	}

	public void execute() {
		try {
			findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@name='q']").sendKeys("hello world");
			Thread.sleep(AppConstants.FIVE_SEC);
			findElementHelper.findMethodBy(ByMethod.XPATH, "//input[@name='btnK']").click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

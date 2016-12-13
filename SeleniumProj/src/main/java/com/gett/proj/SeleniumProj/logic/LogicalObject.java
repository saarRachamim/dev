package com.gett.proj.SeleniumProj.logic;

import org.openqa.selenium.WebDriver;

import com.gett.proj.SeleniumProj.helpers.FindElementHelper;
import com.gett.proj.SeleniumProj.interfaces.LogicalActions;

public abstract class LogicalObject implements LogicalActions{
	FindElementHelper findElementHelper;
	WebDriver driver;
	
	public LogicalObject(FindElementHelper findElementHelper, WebDriver driver) {
		this.findElementHelper = findElementHelper;
		this.driver = driver;
	}
	
	public void navigate(String url){
		driver.navigate().to(url);
	}
}

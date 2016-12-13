package com.gett.proj.SeleniumProj.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gett.proj.SeleniumProj.enums.ByMethod;

public class FindElementHelper {
	private WebDriver driver;

	public FindElementHelper(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement findMethodBy(ByMethod by, String value) {
		switch (by) {
		case XPATH:
			return driver.findElement(By.xpath(value));
		case CSS:
			return driver.findElement(By.cssSelector(value));
		case ID:
			return driver.findElement(By.id(value));
		case NAME:
			return driver.findElement(By.name(value));
		case CLASS:
			return driver.findElement(By.className(value));
		default:
			return null;
		}
	}

}

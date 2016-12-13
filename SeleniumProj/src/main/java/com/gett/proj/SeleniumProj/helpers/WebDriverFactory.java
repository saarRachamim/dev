package com.gett.proj.SeleniumProj.helpers;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gett.proj.SeleniumProj.enums.DriverType;

public class WebDriverFactory {
	DriverType driverType;
	
	public WebDriverFactory(DriverType driverType) {
		this.driverType = driverType;
	}
	
	public WebDriver getWebDriver()
	{
		String seleniumThirdParty = "C:\\selenium\\";
		
		switch (driverType) {
		case FF:
			FirefoxProfile profile = new FirefoxProfile();
			return new FirefoxDriver(profile);
		case CH:
			System.setProperty("webdriver.chrome.driver", seleniumThirdParty + "chromedriver.exe");
			return new ChromeDriver();
		case IE:
			System.setProperty("webdriver.ie.driver",seleniumThirdParty + "IEDriverServer.exe");
			return new InternetExplorerDriver();
		default:
			return null;
		}
	}
}

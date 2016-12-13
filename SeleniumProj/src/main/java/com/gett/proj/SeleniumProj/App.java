package com.gett.proj.SeleniumProj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.sql.rowset.WebRowSet;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.javascript.host.file.FileReader;
import com.gett.proj.SeleniumProj.enums.DriverType;
import com.gett.proj.SeleniumProj.helpers.FindElementHelper;
import com.gett.proj.SeleniumProj.helpers.WebDriverFactory;
import com.gett.proj.SeleniumProj.helpers.XMLReader;
import com.gett.proj.SeleniumProj.interfaces.AppConstants;
import com.gett.proj.SeleniumProj.logic.FacebookLogic;
import com.gett.proj.SeleniumProj.logic.GmailLogic;
import com.gett.proj.SeleniumProj.logic.GoogleLogic;
import com.gett.proj.SeleniumProj.logic.LogicalObject;
import com.google.common.annotations.VisibleForTesting;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * Hello world!
 *
 */
public class App {

	
	public void test() throws IOException {
		XMLReader xmlReader = new XMLReader(AppConstants.relativePath, "test.xml");
		WebDriver driver = new WebDriverFactory(DriverType.FF).getWebDriver();
		FindElementHelper findHelper = new FindElementHelper(driver);

		xmlReader.readFromXMLFile();
		for (int i = 0; i < xmlReader.getNumberOfIteraraions(); i++) {
			String currentUrl = xmlReader.getUrls()[i];
			LogicalObject object = getLogicObject(xmlReader.getNames()[i], findHelper, driver);
			object.navigate(currentUrl);
			object.execute();
		}
	}
	
	public LogicalObject getLogicObject(String objectDesc, FindElementHelper findElementHelper, WebDriver driver){
		if(objectDesc.toLowerCase().equals("gmail"))
			return new GmailLogic(findElementHelper, driver);
		else if(objectDesc.toLowerCase().equals("google"))
			return new GoogleLogic(findElementHelper, driver);
		else if(objectDesc.toLowerCase().equals("facebook"))
			return new FacebookLogic(findElementHelper, driver);
		
		return null;
	}
	
	@Test
	public void androidTest() throws MalformedURLException{
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "/apks/Camera");
		File app = new File(appDir, "app-debug.apk");
		
		AppiumDriver<WebElement> driver;
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus_5_API_24");
		
		cap.setCapability(CapabilityType.PLATFORM, "WINDOWS");

        cap.setCapability("appPackage", "com.example.saar.lesson12inclass");
		cap.setCapability("app", app.getAbsolutePath());
		cap.setCapability("appWaitActivity", ""); 
		
		driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
		
		Assert.assertNotNull(driver.getContext());
	}
}

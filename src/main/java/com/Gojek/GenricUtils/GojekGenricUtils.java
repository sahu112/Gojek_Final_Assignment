package com.Gojek.GenricUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;


public class GojekGenricUtils {
	
	public static Properties prop;
	static long IMPLICITE_WAIT=20;
	static long PAGE_TIMEOUT=20;
	Actions act;
	public static String TESTDATA_SHEET_PATH="D:\\JavaProject\\Gojek_assignment\\src\\main\\java\\com\\Gojek\\TestData\\GojekTestData.xlsx";
	public static Workbook book;
	public static Sheet sheet;
	
	public GojekGenricUtils() {
		
		prop=new Properties();
		try {
			FileInputStream file=new FileInputStream("D:\\JavaProject\\Gojek_assignment\\src\\main\\java\\com\\Gojek\\GenricUtils\\config.properties") ;
			prop.load(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	public void initialization() {
		
		
		String browsername=prop.getProperty("browser");
		
		if(browsername.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "D:\\JavaProject\\Gojek_assignment\\ChromeDriver\\chromedriver.exe");
			Driver.driver=new ChromeDriver();
		}else if(browsername.equals("FF")) {
			System.setProperty("webdriver.gecko.driver", "D:\\JavaProject\\Gojek_assignment\\geckoDriver\\geckodriver.exe");
			Driver.driver=new FirefoxDriver();
			}else if(browsername.equals("IE")) {
				System.setProperty("webdriver.ie.driver", "D:\\JavaProject\\Gojek_assignment\\IEDriver\\IEDriverServer.exe");
				Driver.driver=new InternetExplorerDriver();		
			}
		
		Driver.driver.manage().window().maximize();
		Driver.driver.manage().deleteAllCookies();
		Driver.driver.manage().timeouts().pageLoadTimeout(PAGE_TIMEOUT, TimeUnit.SECONDS);
		Driver.driver.manage().timeouts().implicitlyWait(IMPLICITE_WAIT, TimeUnit.SECONDS);
		Driver.driver.get(prop.getProperty("url"));	
		
		
	}

	public void impliciteWait() {
		Driver.driver.manage().timeouts().implicitlyWait(IMPLICITE_WAIT, TimeUnit.SECONDS);
		
	}
	
	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) Driver.driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));
	}
	
	public static Object[][] getTestData(String sheetName) {
	
		FileInputStream file=null;
		try {
			file=new FileInputStream(TESTDATA_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			book=WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet=book.getSheet(sheetName);
		
		Object [][] data=new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
		//System.out.println(sheet.getLastRowNum() + "--------" +
				// sheet.getRow(0).getLastCellNum());
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
				data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
			
			}
		}
		return data;
	}
	
	
}

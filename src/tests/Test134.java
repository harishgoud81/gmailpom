package tests;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import pages.Composepage;
import pages.Hompepage;
import pages.Loginpage;

public class Test134 
{
public static void main(String[] args) throws Exception
{
	File f=new File("gmaildata.xls");
	Workbook rwb=Workbook.getWorkbook(f);
	Sheet rsh=rwb.getSheet(0);
	int nour=rsh.getRows();
	int nouc=rsh.getColumns();
	
	WritableWorkbook wwb=Workbook.createWorkbook(f,rwb);
	WritableSheet wsh=wwb.getSheet(0);
	
	
	SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
	Date dt=new Date();
	String cname=sf.format(dt);
	Label l1=new Label(nouc, 0, cname);
	wsh.addCell(l1);
	
	for(int i=1;i<nour;i++)
	{
		String u=rsh.getCell(0,i).getContents();
		String uc=rsh.getCell(1,i).getContents();
		String p=rsh.getCell(2,i).getContents();
		String pc=rsh.getCell(3,i).getContents();
		
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Harish\\Downloads\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		
		Hompepage hp=new Hompepage(driver);
		Loginpage lp=new Loginpage(driver);
		Composepage cp=new Composepage(driver);
		
		driver.get("https:www.gmail.com");
		WebDriverWait w=new WebDriverWait(driver, 20);
		w.until(ExpectedConditions.visibilityOf(hp.uid));
		
		hp.filluid(u);
		
		w.until(ExpectedConditions.elementToBeClickable(hp.uidnext));
		hp.clickuidnext();
		Thread.sleep(5000);
		
		try
		{
			if(u.length()==0 && hp.blankuiderr.isDisplayed())
			{
				Label l2=new Label(nouc, i, "blank password test passed");
				wsh.addCell(l2);
			}
			else if(uc.equalsIgnoreCase("invalid") && hp.invaliduiderr.isDisplayed())
			{
				Label l3=new Label(nouc, i, "Invalid user id test passsed");
				wsh.addCell(l3);
			}
			else if(uc.equalsIgnoreCase("valid") && lp.pwd.isDisplayed())
			{
				lp.fillpwd(p);
			
			w.until(ExpectedConditions.elementToBeClickable(lp.pwdnext));
			lp.clickpwdnext();
			 
		if(p.length()==0  && lp.blankpwderr.isDisplayed())
		{
			Label l4=new Label(nouc, i, "blank password test passed");
			wsh.addCell(l4);
		}
		
		else if (p.equalsIgnoreCase("invalid") && lp.invalidpwderr.isDisplayed()) {
			Label l5=new Label(nouc, i, "invalid password test passed");
			wsh.addCell(l5);
		}
		else if (p.equalsIgnoreCase("valid") && cp.comp.isDisplayed()) {
			Label l6=new Label(nouc, i, "valid login");
			wsh.addCell(l6);
			cp.clickprofilepic();
			w.until(ExpectedConditions.elementToBeClickable(cp.signout));
			cp.clicksignout();
		}
		else 
		{
			String fname = sf.format(dt)+".png";
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File dest=new File(fname);
		FileHandler.copy(src, dest);
		Label l7=new Label(nouc, i, "pwd test failed" +fname);
		wsh.addCell(l7);
		}
			}
		
		else 
		{
			String fname=sf.format(dt)+".png";
			File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			File dest=new File(fname);
			FileHandler.copy(src, dest);
			Label l8=new Label(nouc, i, "uid test failed" +fname);
			wsh.addCell(l8);
			
		}
		}
	
	catch(Exception ex)
	{
	Label l9=new Label(nouc, i, ex.getMessage());
	wsh.addCell(l9);
	}
		driver.quit();
	}
	wwb.write();
	wwb.close();
	rwb.close();
	
}
}

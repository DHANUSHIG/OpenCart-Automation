package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

/**
 * Centralized TestNG DataProviders for data-driven testing.
 * Leverages ExcelUtility.getSheetData() to load datasets in a single pass.
 */
public class DataProviders {

	@DataProvider(name = "LoginData")
	public String[][] getdata() throws IOException {
		String path = System.getProperty("user.dir") + "/testData/Opencart_LoginData.xlsx";
		ExcelUtility xlutil = new ExcelUtility(path);
		return xlutil.getSheetData("Sheet1");
	}

}

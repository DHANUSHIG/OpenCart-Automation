package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name="LoginData")
	public String[][] getdata() throws IOException{
		
		String Path = System.getProperty("user.dir") + "/testData/Opencart_LoginData.xlsx";   //taking xl file form the test data
		ExcelUtility xlutil= new ExcelUtility(Path);   //creating the object for the ExcelUtility
		
		int totalrows=xlutil.getRowCount("Sheet1");   //getting the roe count the  from the  sheet
		int totalcols=xlutil.getCellCount("Sheet1", 1);  //getting the cell count form the column
		
		
		String[][] loginData = new String[totalrows][totalcols];//crating the two d array 
		
		for(int i=1;i<=totalrows;i++) //looping to the rows 
		{  
			for(int j=0;j<totalcols;j++)  //looping to the column cell to get data 
			{ 
				loginData[i-1][j]=xlutil.getCellData("Sheet1", i, j); // again storing them in the two  d array
			}
		}
		return loginData;
		
		
	}

}

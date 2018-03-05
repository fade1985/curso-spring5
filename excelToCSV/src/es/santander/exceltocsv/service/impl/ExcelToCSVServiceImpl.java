package es.santander.exceltocsv.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import es.santander.exceltocsv.ExcelToCSVMain;
import es.santander.exceltocsv.model.CellPosition;
import es.santander.exceltocsv.service.ExcelToCSVService;

public class ExcelToCSVServiceImpl implements ExcelToCSVService {
    
    private static final Logger LOGGER = Logger.getLogger(ExcelToCSVMain.class);
    
    private Workbook wb;
    
    @Override
    public void processFile(
        final String documentPath){
        
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(documentPath);
            wb = new HSSFWorkbook(inputStream);
            CellPosition cp = findCell("DNI");
        } catch (Exception e) {
            LOGGER.debug("Exception.", e);
        } finally {
            if (inputStream != null) {
                safeClose(inputStream);
            }
        }
    }
    
    private CellPosition findCell(
        final String value){
        
        String toFind = value;
        CellPosition cp = null;
        
        DataFormatter formatter = new DataFormatter();
        Sheet sheet1 = wb.getSheetAt(0);
        for (Row row : sheet1) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                
                // get the text that appears in the cell by getting the cell
                // value and applying any data formats (Date, 0.00, 1.23e9,
                // $1.23, etc)
                String text = formatter.formatCellValue(cell);
                
                // is it an exact match?
                if (toFind.equals(text)) {
                    System.out.println("Text matched at " + cellRef.formatAsString());
                    return CellPosition.builder().posX(row.getRowNum()).posY(cell.getColumnIndex()).build();
                }
                // is it a partial match?
                else if (text.contains(toFind)) {
                    System.out.println("Text found as part of " + cellRef.formatAsString());
                    return CellPosition.builder().posX(row.getRowNum()).posY(cell.getColumnIndex()).build();
                }
            }
        }
        
        return cp;
    }
    
    @Override
    public void testSelenium(
        final String url) throws MalformedURLException, URISyntaxException, InterruptedException{
        
        LOGGER.info("Test Selenium");
        
        InputStream in = getClass().getResourceAsStream("/drivers/chromedriver.exe");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        LOGGER.info("Leido como InputStream");
        
        System.setProperty("webdriver.chrome.driver", "/drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        /*
         * String downloadFilepath =
         * "C:\\Users\\jose.marcos\\Desktop\\excelToCSV\\excel"; HashMap<String,
         * Object> chromePrefs = new HashMap<String, Object>();
         * chromePrefs.put("profile.default_content_settings.popups", 0);
         * chromePrefs.put("download.default_directory", downloadFilepath);
         * ChromeOptions options = new ChromeOptions();
         * options.setExperimentalOption("prefs", chromePrefs);
         * DesiredCapabilities cap = DesiredCapabilities.chrome();
         * cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
         * cap.setCapability(ChromeOptions.CAPABILITY, options); WebDriver
         * driver = new ChromeDriver(cap);
         */
        // driver.get("http://www.google.es");
        driver.get(url);
        System.out.println("Title of the page is -> " + driver.getPageSource());
        Thread.sleep(1000);
        // Find the text input element by its name
        WebElement element = driver.findElement(By.id("query"));
        if (element != null) {
            LOGGER.info("TODO OK, dentro del if");
            element.sendKeys("SELECT * FROM AUX_DES");
            System.err.println("Escribo texto" + element.getText());
            List<WebElement> buttons = driver.findElements(By.className("btn"));
            if (buttons != null) {
                for (WebElement webElement : buttons) {
                    if (webElement.getText().contains("Ejecutar")) {
                        webElement.click();
                        System.err.println("Click button" + webElement.getText());
                        break;
                    }
                }
            }
            Thread.sleep(1000);
            WebElement buttonEx = driver.findElement(By.id("expExcel"));
            if (buttonEx != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", buttonEx);
                buttonEx.click();
                System.err.println("Click button excel");
            }
        }
        
        // Enter something to search for
        // element.sendKeys("Cheese!");
        
        // Now submit the form. WebDriver will find the form for us from the
        // element
        // element.submit();
        
        // Check the title of the page
        System.err.println("Page title is: " + driver.getTitle());
        
        driver.quit();
    }
    
    public void safeClose(
        InputStream fis){
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                LOGGER.debug("Exception.", e);
            }
        }
    }
}

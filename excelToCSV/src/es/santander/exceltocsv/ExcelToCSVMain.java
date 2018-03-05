package es.santander.exceltocsv;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.log4j.BasicConfigurator;

import es.santander.exceltocsv.service.ExcelToCSVService;
import es.santander.exceltocsv.service.impl.ExcelToCSVServiceImpl;

public class ExcelToCSVMain {
    
    public static void main(
        String[] args) throws MalformedURLException, URISyntaxException, InterruptedException{
        
        BasicConfigurator.configure();
        
        ExcelToCSVService service = new ExcelToCSVServiceImpl();
        
        // service.processFile(args[0]);
        service.testSelenium(args[0]);
    }
    
}

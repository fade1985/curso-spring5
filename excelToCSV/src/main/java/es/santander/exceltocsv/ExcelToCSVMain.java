package es.santander.exceltocsv;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.BasicConfigurator;

import es.santander.exceltocsv.service.ExcelToCSVService;
import es.santander.exceltocsv.service.impl.ExcelToCSVServiceImpl;

public class ExcelToCSVMain {
    
    public static void main(
        String[] args) throws URISyntaxException, InterruptedException, IOException{
        
        BasicConfigurator.configure();
        
        ExcelToCSVService service = new ExcelToCSVServiceImpl();
        
        // service.processFile(args[0]);
        // service.testSelenium("http://www.google.es");
        service.pdfToCsv("C:\\Cosas_Importantes\\2018\\Marzo\\Seguros\\Excels\\VIVA_REAL.pdf");
    }
    
}

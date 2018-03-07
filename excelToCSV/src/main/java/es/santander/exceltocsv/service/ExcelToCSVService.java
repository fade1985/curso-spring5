package es.santander.exceltocsv.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface ExcelToCSVService {
    
    void processFile(
        final String documentPath);
    
    void testSelenium(
        final String url) throws MalformedURLException, URISyntaxException, InterruptedException, IOException;
    
    void pdfToCsv(
        final String string);
}

package es.santander.exceltocsv.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface ExcelToCSVService {
    
    void processFile(
        final String documentPath);
    
    void testSelenium(
        final String url) throws MalformedURLException, URISyntaxException, InterruptedException;
}

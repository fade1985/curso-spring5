package es.santander.exceltocsv.service;

public interface DuplicateService {
    
    void duplicaArag(
        final String inPath,
        final String outPath,
        final String clientsPath);
    
}

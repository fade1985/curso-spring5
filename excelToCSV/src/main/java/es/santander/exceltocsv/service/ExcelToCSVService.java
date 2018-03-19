package es.santander.exceltocsv.service;

public interface ExcelToCSVService {
    
    void transformAviva(
        final String inPath,
        final String outPath);
    
    void transformArag(
        final String inPath,
        final String outPath);
    
    void transformPelayo(
        final String inPath,
        final String outPath);
    
    void transformSanitas(
        final String inPath,
        final String outPath);
}

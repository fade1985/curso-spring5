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
    
    void transformCigna(
        final String inPath,
        final String inPath2,
        final String outPath);
    
    void transformAwp(
        final String inPath,
        final String outPath);
    
    void transformSantaLucia(
        final String inPath,
        final String outPath);
    
    void transformAllianz(
        final String inPath,
        final String outPath);
    
    void transformMapfreEsp(
        final String inPath,
        final String outPath);
    
}

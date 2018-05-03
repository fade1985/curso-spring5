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
        
        service.transformAviva("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/aviva.pdf",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformArag("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/arag.xls",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformPelayo("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/pelayo.xlsx",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformSanitas("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/sanitas.xls",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformCigna("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/cigna_bajas.txt",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/cigna_billing.txt",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformAwp("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/awp.xlsx",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        
        service.transformSantaLucia("C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/entrada/santaLucia.txt",
                "C:/Cosas_Importantes/2018/Marzo/Seguros/Pruebas/salida/");
        // String entrada =
        // "Entrada:::: uno: '&&1' , dos: '&&2' , varios: '&&123', dos: g4agerga
        // '&&2', dos: '&&2', uno: '&&1'";
        // System.out.println(entrada);
        //
        // Map<String, String> map = new HashMap<String, String>();
        // // entrada = entrada.replaceAll("'&&(\\d)+'", "?");
        // String split[] = entrada.split("'&&");
        // for (String string : split) {
        // String value[] = string.split("'");
        // if (StringUtils.isNumeric(value[0])) {
        // map.put("'&&".concat(value[0].concat("'")), StringUtils.EMPTY);
        // }
        //
        // }
        // System.out.println(entrada);
    }
    
}

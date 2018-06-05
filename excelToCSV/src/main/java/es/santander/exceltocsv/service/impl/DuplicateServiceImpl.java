package es.santander.exceltocsv.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import es.santander.exceltocsv.model.CellPosition;
import es.santander.exceltocsv.service.DuplicateService;
import es.santander.exceltocsv.utilities.Constants;

public class DuplicateServiceImpl implements DuplicateService {
    
    private static final Logger LOGGER = Logger.getLogger(DuplicateServiceImpl.class);
    
    private Workbook wb;
    
    @Override
    public void duplicaArag(
        String inPath,
        String outPath,
        String clientsPath){
        
        // Stream para la excel
        InputStream inputStream = null;
        
        // Fichero de clientes
        FileReader fileReader = null;
        
        // Reader para el fichero de clientes
        BufferedReader br = null;
        
        try {
            File f = new File(inPath);
            File[] matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(
                    File dir,
                    String name){
                    return name.startsWith("arag");
                }
            });
            // Generamos las dos copias
            File inputFile = new File(matchingFiles[0].getPath());
            String ext = FilenameUtils.getExtension(inputFile.getPath());
            Path path1 =
                    Paths.get(outPath.concat(Constants.ARAG_FILE).concat(Constants.CLIENT).concat(".").concat(ext));
            Path path2 =
                    Paths.get(outPath.concat(Constants.ARAG_FILE).concat(Constants.NO_CLIENT).concat(".").concat(ext));
            // Files.copy(inputFile,
            // new File(path1.toString()));
            // Files.copy(inputFile,
            // new File(path2.toString()));
            
            // Lectura clientes
            System.setProperty("line.separator", "\n");
            
            // Leemos el fichero de entrada1
            fileReader = new FileReader(clientsPath);
            br = new BufferedReader(fileReader);
            
            List<String> clients = new ArrayList<>();
            
            String sCurrentLine;
            
            while ((sCurrentLine = br.readLine()) != null) {
                clients.add(sCurrentLine);
            }
            br.close();
            fileReader.close();
            
            // Buscamos el inicio de filas de arag
            // Leemos el fichero de entrada
            inputStream = new FileInputStream(inputFile);
            wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            // Recuperamos la posición de la primera celda
            CellPosition cp = findCell(Constants.ARAG_INIT);
            
            // Nos situamos en la primera celda con datos
            cp.setPosX(cp.getPosX() + 1);
            Cell cell = sheet.getRow(cp.getPosX()).getCell(4);
            
            // List<Integer> posCliente = new ArrayList<>();
            // List<Integer> posNoCliente = new ArrayList<>();
            //
            // int i = 0;
            // while (cell != null) {
            // if (clients.contains(cell.toString())) {
            // posCliente.add(i);
            // } else {
            // posNoCliente.add(i);
            // }
            // i++;
            // // Avanzamos a la siguiente fila
            // cell = sheet.getRow(cp.getPosX() + i).getCell(4);
            // }
            // inputStream.close();
            
            Workbook wbClients = WorkbookFactory.create(inputFile);
            // Con los dos arrays abrimos los ficheros resultantes y eliminamos
            // filas
            // Recuperamos la posición de la primera celda
            cp = findCell(Constants.ARAG_INIT);
            cp.setPosX(cp.getPosX() + 1);
            cell = wbClients.getSheetAt(0).getRow(cp.getPosX()).getCell(4);
            // int i = 0;
            // while (cell != null) {
            // if (clients.contains(cell.toString())) {
            // wbClients.getSheetAt(0).shiftRows(cp.getPosX() + i, cp.getPosX()
            // + i, -1);
            // }
            // i++;
            // // Avanzamos a la siguiente fila
            // cell = wbClients.getSheetAt(0).getRow(cp.getPosX() +
            // i).getCell(4);
            // }
            sheet = wbClients.getSheetAt(0);
            for (int i = cp.getPosX() + 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                cell = row.getCell(4);
                if (cell != null && clients.contains(cell.toString())) {
                    wbClients.getSheetAt(0).shiftRows(row.getRowNum() + 1, sheet.getLastRowNum() + 1, -1);
                    i--;
                }
            }
            // for (int pos : posCliente) {
            // wbClients.getSheetAt(0).removeRow(wbClients.getSheetAt(0).getRow(cp.getPosX()
            // + pos));
            // }
            
            Workbook exceldoc = wbClients;
            FileOutputStream out = new FileOutputStream(path1.toString());
            exceldoc.write(out);
            out.close();
            
            Workbook wbNoClients = WorkbookFactory.create(inputFile);
            // Con los dos arrays abrimos los ficheros resultantes y
            // eliminamos
            // filas
            sheet = wbNoClients.getSheetAt(0);
            for (int i = cp.getPosX() + 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                cell = row.getCell(4);
                if (cell != null && !clients.contains(cell.toString())) {
                    wbNoClients.getSheetAt(0).shiftRows(row.getRowNum() + 1, sheet.getLastRowNum() + 1, -1);
                    i--;
                }
            }
            
            Workbook exceldoc2 = wbNoClients;
            FileOutputStream out2 = new FileOutputStream(path2.toString());
            exceldoc2.write(out2);
            out2.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                safeClose(br);
            }
            if (fileReader != null) {
                safeClose(fileReader);
            }
            if (inputStream != null) {
                safeClose(inputStream);
            }
        }
    }
    
    private void safeClose(
        FileReader fr){
        if (fr != null) {
            try {
                fr.close();
            } catch (IOException e) {
                LOGGER.debug("Exception.", e);
            }
        }
    }
    
    private void safeClose(
        BufferedReader br){
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                LOGGER.debug("Exception.", e);
            }
        }
    }
    
    private void safeClose(
        InputStream fis){
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                LOGGER.debug("Exception.", e);
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
                if (toFind.equalsIgnoreCase(text)) {
                    System.out.println("Text matched at " + cellRef.formatAsString());
                    return CellPosition.builder().posX(row.getRowNum()).posY(cell.getColumnIndex()).build();
                }
                // is it a partial match?
                else if (text.equalsIgnoreCase(toFind)) {
                    System.out.println("Text found as part of " + cellRef.formatAsString());
                    return CellPosition.builder().posX(row.getRowNum()).posY(cell.getColumnIndex()).build();
                }
            }
        }
        
        return cp;
    }
}

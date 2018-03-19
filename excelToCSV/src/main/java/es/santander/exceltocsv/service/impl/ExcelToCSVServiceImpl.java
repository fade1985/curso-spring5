package es.santander.exceltocsv.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import es.santander.exceltocsv.ExcelToCSVMain;
import es.santander.exceltocsv.model.CellPosition;
import es.santander.exceltocsv.service.ExcelToCSVService;
import es.santander.exceltocsv.utilities.Constants;

public class ExcelToCSVServiceImpl implements ExcelToCSVService {
    
    private static final Logger LOGGER = Logger.getLogger(ExcelToCSVMain.class);
    
    private final NumberFormat formatNumber = NumberFormat.getInstance(new Locale("es", "ES"));
    private final DecimalFormat format = new DecimalFormat("+0000000000000;-0000000000000");
    private final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    
    private Workbook wb;
    
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
    
    @Override
    public void transformAviva(
        final String inPath,
        final String outPath){
        
        try {
            // Creamos el fichero de salida
            System.setProperty("line.separator", "\n");
            PrintWriter pw = new PrintWriter(new File(outPath.concat(Constants.AVIVA_FILE).concat(Constants.CSV)));
            
            // Leemos el fichero de entrada
            PdfReader reader;
            reader = new PdfReader(inPath);
            
            // Convertimos el pdf a texto
            PdfReaderContentParser parser = new PdfReaderContentParser(
                    reader);
            TextExtractionStrategy strategy;
            String text = null;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i,
                        new SimpleTextExtractionStrategy());
                text = strategy.getResultantText();
            }
            
            // Pasamos el texto convertido a lineas
            List<String> lines = new ArrayList<String>(Arrays.asList(text.split(Constants.LINE_BREAK)));
            
            // Recorremos el array buscando el principio de los datos
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(0).contains(Constants.AVIVA_INIT)) {
                    lines.remove(0);
                    break;
                }
                lines.remove(0);
            }
            
            for (String line : lines) {
                String[] data = line.split(Constants.SPACES);
                if (data.length > 5) {
                    pw.println(insertAvivaData(data));
                }
            }
            reader.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void transformPelayo(
        final String inPath,
        final String outPath){
        
        InputStream inputStream = null;
        
        try {
            // Creamos el fichero de salida
            System.setProperty("line.separator", "\n");
            PrintWriter pw = new PrintWriter(new File(outPath.concat(Constants.PELAYO_FILE).concat(Constants.CSV)));
            
            // Leemos el fichero de entrada
            inputStream = new FileInputStream(inPath);
            wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            
            // Recuperamos la posición de la primera celda
            CellPosition cp = findCell(Constants.PELAYO_INIT);
            
            // Nos situamos en la primera celda con datos
            cp.setPosX(cp.getPosX() + 1);
            Cell cell = sheet.getRow(cp.getPosX()).getCell(1);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PELAYO_DATE_FORMAT);
            
            int i = 0;
            while (cell != null) {
                StringBuilder sb = new StringBuilder();
                Row row = sheet.getRow(cp.getPosX() + i);
                
                // Recorremos la fila
                for (int j = 1; j < 13; j++) {
                    cell = row.getCell(j);
                    
                    String cellData = getExcelData(cell);
                    
                    if (j == 1) { // Ramo
                        sb.append(StringUtils
                                .rightPad(cellData, Constants.PELAYO_TYPE_LENGTH, StringUtils.SPACE));
                    } else if (j == 2) { // Recibo
                        
                        sb.append(StringUtils
                                .rightPad(cellData, Constants.PELAYO_RECEIPT_LENGTH, StringUtils.SPACE));
                    } else if (j == 3 || j == 5) {
                        // Fechas
                        Date date = dateFormat.parse(cellData);
                        sb.append(sdf.format(date));
                    } else if (j == 4) {
                        String data[] = cellData.split(StringUtils.SPACE);
                        String policy = data[0] == null ? StringUtils.SPACE : data[0];
                        String payMethod = data[1] == null ? StringUtils.SPACE : data[1];
                        sb.append(
                                StringUtils.rightPad(policy,
                                        Constants.PELAYO_POLICY_LENGTH,
                                        StringUtils.SPACE))
                                .append(Constants.SEMICOLON)
                                .append(payMethod);
                    } else if (j == 6 || j == 7 || j == 11) {
                        // importes
                        sb.append(insertNumber(cellData, false));
                    } else if (j == 9) {
                        // cliente
                        sb.append(StringUtils
                                .rightPad(cellData, Constants.NAME_LENGTH, StringUtils.SPACE));
                    } else if (j == 12) {
                        // p.venta
                        Double doubleValue = new Double(cellData);
                        sb.append(StringUtils.rightPad(String.valueOf(doubleValue.intValue()),
                                Constants.PELAYO_STALL_LENGTH,
                                StringUtils.SPACE));
                    } else {
                        // resto de campos
                        sb.append(cellData);
                    }
                    
                    sb.append(Constants.SEMICOLON);
                    
                }
                pw.println(sb.toString());
                i++;
                
                // Avanzamos a la siguiente fila
                if (sheet.getRow(cp.getPosX() + i) == null) {
                    break;
                } else {
                    cell = sheet.getRow(cp.getPosX() + i).getCell(1);
                }
            }
            
            inputStream.close();
            pw.close();
        } catch (Exception e) {
            LOGGER.debug("Exception.", e);
        } finally {
            if (inputStream != null) {
                safeClose(inputStream);
            }
        }
    }
    
    public void transformSanitas(
        final String inPath,
        final String outPath){
        
        InputStream inputStream = null;
        
        try {
            // Creamos el fichero de salida
            System.setProperty("line.separator", "\n");
            PrintWriter pw = new PrintWriter(new File(outPath.concat(Constants.SANITAS_FILE).concat(Constants.CSV)));
            
            // Leemos el fichero de entrada
            inputStream = new FileInputStream(inPath);
            wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            
            // Recuperamos la posición de la primera celda
            CellPosition cp = findCell(Constants.SANITAS_INIT);
            
            // Nos situamos en la primera celda con datos
            cp.setPosX(cp.getPosX() + 1);
            Cell cell = sheet.getRow(cp.getPosX()).getCell(0);
            
            int i = 0;
            while (cell != null) {
                StringBuilder sb = new StringBuilder();
                Row row = sheet.getRow(cp.getPosX() + i);
                String collective = StringUtils.EMPTY;
                
                // Recorremos la fila
                for (int j = 0; j < 38; j++) {
                    cell = row.getCell(j);
                    
                    String cellData = getExcelData(cell);
                    
                    // if (j == 0 || j == 1 || j == 24) {
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.SANITAS_DATA_LENGTH,
                    // StringUtils.SPACE));
                    // } else if (j == 2 || j == 8 || j == 18 || j == 19) {
                    // // Fechas
                    // if (cellData.equals(StringUtils.SPACE)) {
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.SHORT_LENGTH,
                    // StringUtils.SPACE));
                    // } else {
                    // Date date =
                    // HSSFDateUtil.getJavaDate(Double.parseDouble(cellData));
                    // sb.append(sdf.format(date));
                    // }
                    // } else if (j == 3) {
                    // Double doubleValue = new Double(cellData);
                    // sb.append(StringUtils.leftPad(String.valueOf(doubleValue.intValue()),
                    // Constants.SANITAS_COLLECTIVE_LENGTH,
                    // "0"));
                    // } else if (j == 4 || j == 5 || j == 21 || j == 22 || j ==
                    // 27 || j == 28 || j == 29 || j == 30
                    // || j == 31 || j == 32 || j == 33 || j == 34 || j == 36) {
                    // if (cellData.equals(StringUtils.SPACE)) {
                    // sb.append(StringUtils.rightPad(cellData,
                    // Constants.SANITAS_ENTITY_LENGTH,
                    // StringUtils.SPACE));
                    // } else {
                    // Double doubleValue = new Double(cellData);
                    // sb.append(StringUtils.rightPad(String.valueOf(doubleValue.intValue()),
                    // Constants.SANITAS_ENTITY_LENGTH,
                    // StringUtils.SPACE));
                    // }
                    // } else if (j == 6) {
                    // if (cellData.equals(StringUtils.SPACE)) {
                    // sb.append(StringUtils.rightPad(cellData,
                    // Constants.SANITAS_POLICY_LENGTH,
                    // StringUtils.SPACE));
                    // } else {
                    // Double doubleValue = new Double(cellData);
                    // sb.append(StringUtils.rightPad(String.valueOf(doubleValue.intValue()),
                    // Constants.SANITAS_POLICY_LENGTH,
                    // StringUtils.SPACE));
                    // }
                    // } else if (j == 7 || j == 35 || j == 37) {
                    // // cliente
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.NAME_LENGTH,
                    // StringUtils.SPACE));
                    // } else if (j == 9) {
                    // Double doubleValue = new Double(cellData);
                    // sb.append(StringUtils.rightPad(String.valueOf(doubleValue.intValue()),
                    // Constants.SANITAS_COMMISSION_LENGTH,
                    // StringUtils.SPACE));
                    // } else if (j == 10 || j == 11 || j == 12 || j == 13 || j
                    // == 14) {
                    // // importes
                    // sb.append(insertNumber(cellData, false));
                    // } else if (j == 15 || j == 16) {
                    // // ramo
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.MEDIUM_LENGTH,
                    // StringUtils.SPACE));
                    // } else if (j == 17 || j == 20) {
                    // // ramo
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.NAME_LENGTH,
                    // StringUtils.SPACE));
                    // } else if (j == 32) {
                    // // ramo
                    // sb.append(StringUtils
                    // .rightPad(cellData, Constants.SANITAS_ENTITY_LENGTH,
                    // StringUtils.SPACE));
                    // } else {
                    // sb.append(cellData);
                    // }
                    
                    if (j == 2 || j == 8 || j == 18 || j == 19) {
                        // Fechas
                        if (cellData.equals(StringUtils.SPACE)) {
                            sb.append(StringUtils
                                    .rightPad(cellData, Constants.SHORT_LENGTH, StringUtils.SPACE));
                        } else {
                            Date date = HSSFDateUtil.getJavaDate(Double.parseDouble(cellData));
                            sb.append(sdf.format(date));
                        }
                        sb.append(Constants.SEMICOLON);
                    } else if (j == 3) {
                        // Colectivo
                        if (cellData.equals(StringUtils.SPACE)) {
                            collective = StringUtils.EMPTY;
                        } else {
                            Double doubleValue = new Double(cellData);
                            collective = String.valueOf(doubleValue.intValue());
                        }
                        collective = StringUtils
                                .leftPad(collective, Constants.FIVE_LENGTH, "0");
                    } else if (j == 6) {
                        // Poliza
                        if (cellData.equals(StringUtils.SPACE)) {
                            sb.append(StringUtils
                                    .rightPad(collective,
                                            Constants.SANITAS_POLICY_LENGTH,
                                            StringUtils.SPACE));
                        } else {
                            Double doubleValue = new Double(cellData);
                            String policy = String.valueOf(doubleValue.intValue());
                            sb.append(StringUtils
                                    .rightPad(policy.concat(collective),
                                            Constants.SANITAS_POLICY_LENGTH,
                                            StringUtils.SPACE));
                        }
                        sb.append(Constants.SEMICOLON);
                    } else if (j == 7) {
                        // Nombre cliente
                        sb.append(StringUtils
                                .rightPad(cellData,
                                        Constants.NAME_LENGTH,
                                        StringUtils.SPACE));
                        sb.append(Constants.SEMICOLON);
                    } else if (j == 10 || j == 11 || j == 14) {
                        // Importes
                        sb.append(insertNumber(cellData, false));
                        sb.append(Constants.SEMICOLON);
                    } else if (j == 15 || j == 16 || j == 17) {
                        // Tipo producto
                        sb.append(StringUtils
                                .rightPad(cellData,
                                        Constants.MEDIUM_LENGTH,
                                        StringUtils.SPACE));
                        sb.append(Constants.SEMICOLON);
                    } else if (j == 20) {
                        // Forma de pago
                        sb.append(StringUtils
                                .rightPad(cellData,
                                        Constants.FIVE_LENGTH,
                                        StringUtils.SPACE));
                        sb.append(Constants.SEMICOLON);
                    }
                    
                }
                sb.deleteCharAt(sb.length() - 1);
                pw.println(sb.toString());
                i++;
                
                // Avanzamos a la siguiente fila
                if (sheet.getRow(cp.getPosX() + i) == null) {
                    break;
                } else {
                    cell = sheet.getRow(cp.getPosX() + i).getCell(0);
                }
                
            }
            
            inputStream.close();
            pw.close();
        } catch (Exception e) {
            LOGGER.debug("Exception.", e);
        } finally {
            if (inputStream != null) {
                safeClose(inputStream);
            }
        }
    }
    
    public void transformArag(
        final String inPath,
        final String outPath){
        
        InputStream inputStream = null;
        
        try {
            // Creamos el fichero de salida
            System.setProperty("line.separator", "\n");
            PrintWriter pw = new PrintWriter(new File(outPath.concat(Constants.ARAG_FILE).concat(Constants.CSV)));
            
            // Leemos el fichero de entrada
            inputStream = new FileInputStream(inPath);
            wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            
            // Recuperamos la posición de la primera celda
            CellPosition cp = findCell(Constants.ARAG_INIT);
            
            // Nos situamos en la primera celda con datos
            cp.setPosX(cp.getPosX() + 1);
            Cell cell = sheet.getRow(cp.getPosX()).getCell(0);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ARAG_DATE_FORMAT);
            
            int i = 0;
            while (cell != null) {
                StringBuilder sb = new StringBuilder();
                Row row = sheet.getRow(cp.getPosX() + i);
                
                // Recorremos la fila
                for (int j = 0; j < 10; j++) {
                    cell = row.getCell(j);
                    
                    String cellData = getExcelData(cell);
                    
                    if (j == 3) {
                        sb.append(StringUtils
                                .rightPad(cell.getStringCellValue(),
                                        Constants.NAME_LENGTH,
                                        StringUtils.SPACE));
                    } else if (j > 6) {
                        sb.append(insertNumber(cellData, false));
                    } else if (j == 5 || j == 6) {
                        Date date = dateFormat.parse(cellData);
                        sb.append(sdf.format(date));
                    } else {
                        sb.append(cellData);
                    }
                    sb.append(Constants.SEMICOLON);
                }
                sb.deleteCharAt(sb.length() - 1);
                pw.println(sb.toString());
                i++;
                
                // Avanzamos a la siguiente fila
                cell = sheet.getRow(cp.getPosX() + i).getCell(0);
                
            }
            
            inputStream.close();
            pw.close();
        } catch (Exception e) {
            LOGGER.debug("Exception.", e);
        } finally {
            if (inputStream != null) {
                safeClose(inputStream);
            }
        }
    }
    
    private String insertAvivaData(
        String[] data) throws ParseException{
        
        StringBuilder sb = new StringBuilder();
        StringBuilder name = new StringBuilder();
        
        boolean processedName = false;
        boolean existName = true;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.AVIVA_DATE_FORMAT);
        
        for (int i = 0; i < data.length; i++) {
            if (i == 0 || i == 1) {
                Date date = dateFormat.parse(data[i]);
                sb.append(sdf.format(date));
                sb.append(Constants.SEMICOLON);
            } else if (i == 5 && data[i].matches(Constants.NUMBER_FORMAT)) {
                // NO existe nombre, añadimos un vacío y el primer importe
                sb.append(StringUtils.rightPad(StringUtils.EMPTY, Constants.NAME_LENGTH, StringUtils.SPACE));
                sb.append(Constants.SEMICOLON);
                sb.append(insertNumber(data[i], true));
                existName = false;
            } else if (i >= 5 && !data[i].matches(Constants.NUMBER_FORMAT)) {
                // Añadimos campos al nombre del tomador
                name.append(data[i]);
                name.append(StringUtils.SPACE);
            } else if (i > 5 && data[i].matches(Constants.NUMBER_FORMAT)) {
                // Eliminamos el último espacio dejado por el nombre, lo
                // añadimos rellenando espacios en blanco, y añadimos el primer
                // importe
                if (!processedName && existName) {
                    name.deleteCharAt(name.length() - 1);
                    sb.append(StringUtils
                            .rightPad(name.toString(),
                                    Constants.NAME_LENGTH,
                                    StringUtils.SPACE));
                    processedName = true;
                }
                sb.append(Constants.SEMICOLON);
                sb.append(insertNumber(data[i], true));
            } else if (i == 3) {
                // campo referencia
                sb.append(StringUtils.rightPad(data[i], Constants.AVIVA_REFERENCE_LENGTH, StringUtils.EMPTY));
                sb.append(Constants.SEMICOLON);
            } else {
                
                // 5 primeros campos, excepto el cuarto
                sb.append(data[i]);
                sb.append(Constants.SEMICOLON);
            }
        }
        
        return sb.toString();
    }
    
    private String insertNumber(
        String data,
        boolean isFormat){
        
        double finalDouble = 0d;
        
        try {
            if (isFormat) {
                finalDouble = formatNumber.parse(data).doubleValue() * 100;
            } else {
                finalDouble = Double.parseDouble(data) * 100;
            }
            return format.format(finalDouble);
        } catch (ParseException e) {
            LOGGER.error("Error al parsear el importe: ".concat(data));
            return StringUtils.EMPTY;
        }
    }
    
    private String getExcelData(
        Cell cell){
        if (cell == null) {
            return StringUtils.SPACE;
        } else {
            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case STRING:
                    return cell.getStringCellValue().trim().equals(StringUtils.EMPTY) ? StringUtils.SPACE
                            : cell.getStringCellValue().trim();
                case BLANK:
                    return StringUtils.EMPTY;
                case _NONE:
                case FORMULA:
                    return StringUtils.SPACE;
                case ERROR:
                    System.out.println(cell.getErrorCellValue());
                    return StringUtils.EMPTY;
                default:
                    return StringUtils.EMPTY;
            }
        }
        
    }
}

package es.santander.exceltocsv.utilities;

public class Constants {
    
    // GENERALES
    public static final String CSV = ".csv";
    public static final String SEMICOLON = ";";
    public static final String SPACES = "\\s+";
    public static final String LINE_BREAK = "\n";
    public static final int NAME_LENGTH = 100;
    public static final int MEDIUM_LENGTH = 50;
    public static final int SHORT_LENGTH = 10;
    public static final int FIVE_LENGTH = 5;
    public static final String NUMBER_FORMAT = "^([+\\-]?[0-9\\.]*(\\,[0-9]*)?)$";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // ARAG
    public static final String ARAG_INIT = "RECIBO";
    public static final String ARAG_FILE = "arag";
    public static final String ARAG_DATE_FORMAT = "dd-MM-yy";
    
    // AVIVA
    public static final String AVIVA_INIT = "F. Mov";
    public static final String AVIVA_FILE = "aviva";
    public static final int AVIVA_REFERENCE_LENGTH = 40;
    public static final String AVIVA_DATE_FORMAT = "dd-MMM-yy";
    
    // PELAYO
    public static final String PELAYO_INIT = "Ramo";
    public static final String PELAYO_FILE = "pelayo";
    public static final int PELAYO_RECEIPT_LENGTH = 14;
    public static final int PELAYO_POLICY_LENGTH = 40;
    public static final String PELAYO_DATE_FORMAT = "dd-MM-yyyy";
    public static final int PELAYO_TYPE_LENGTH = 10;
    public static final int PELAYO_STALL_LENGTH = 20;
    
    // SANITAS
    public static final String SANITAS_INIT = "INFORME";
    public static final String SANITAS_FILE = "sanitas";
    public static final int SANITAS_DATA_LENGTH = 40;
    public static final String SANITAS_DATE_FORMAT = "dd/MM/yyyy";
    public static final int SANITAS_ENTITY_LENGTH = 10;
    public static final int SANITAS_POLICY_LENGTH = 40;
    public static final int SANITAS_COMMISSION_LENGTH = 3;
    public static final int SANITAS_PRODUCT_TYPE = 2;
    
}

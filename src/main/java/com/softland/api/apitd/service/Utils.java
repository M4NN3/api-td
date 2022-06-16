package com.softland.api.apitd.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final int ROW_PER_PAGE = 20;
    public static final String ESTADO_CIVIL = "Soltero/a";
    public static final int DIVISION_BIENES = 0;

    public static Map<String, String> getClavePredial(String clavePredial) {
        Map<String, String> claveSplit = new HashMap<>();
        claveSplit.put("provincia", clavePredial.substring(0, 2));
        claveSplit.put("canton", clavePredial.substring(2, 4));
        claveSplit.put("parroquia", clavePredial.substring(4, 6));
        claveSplit.put("zona", clavePredial.substring(6, 9));
        claveSplit.put("sector", clavePredial.substring(9, 12));
        claveSplit.put("poligono", clavePredial.substring(12, 15));
        claveSplit.put("predio", clavePredial.substring(15, 18));
        claveSplit.put("bloque", clavePredial.substring(18, 21));
        claveSplit.put("piso", clavePredial.substring(21, 24));
        claveSplit.put("prophorizontal", clavePredial.substring(24));
        return claveSplit;
    }
    public static void logConsole(String message){
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:ms"));
        System.out.println(String.format("- [%1$s] %2$s", dateTime, message));
    }

    public static LocalDate getCurrentDate(){
        return LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ISO_DATE), DateTimeFormatter.ISO_DATE);
    }
    public static LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now();
    }

    //01-02-50-003-002-002-010-000-000-000
    //prov: 01
    //canton: 02
    //parroquia: 50
    //zona: 003
    //sector: 002
    //poligono: 002
    //predio: 010
    //bloque: 000
    //piso: 000
    //propHorizontal: 000
}

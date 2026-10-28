package com.smartkeeda.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ExcelUtils {
    
    public static String getCellData(int rowNum, int colNum) {
        // We are now reading a CSV file instead of an Excel file
        String filePath = "src/test/resources/testdata.csv";
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new RuntimeException("Data file not found at: " + file.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int currentRow = 0;
            
            while ((line = br.readLine()) != null) {
                if (currentRow == rowNum) {
                    // Split by comma
                    String[] values = line.split(",");
                    if (colNum < values.length) {
                        return values[colNum].trim();
                    }
                }
                currentRow++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read data file at " + file.getAbsolutePath(), e);
        }
        
        return "";
    }
}
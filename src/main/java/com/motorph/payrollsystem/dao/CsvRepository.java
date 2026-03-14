/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author djjus
 */
public interface CsvRepository {
    String getCsvHeader();
    void initializeStorage() throws IOException;
    String[] parseCsvLine(String line);
    void appendCsvRow(String row) throws IOException;
    void rewriteCsvRows(List<String> rows) throws IOException;
}

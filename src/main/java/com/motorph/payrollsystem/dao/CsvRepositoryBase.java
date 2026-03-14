/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public abstract class CsvRepositoryBase implements CsvRepository {
    protected final Path csvPath;

    protected CsvRepositoryBase(Path csvPath) {
        this.csvPath = csvPath;
    }

    protected abstract String getHeader();
    
    @Override
    public String getCsvHeader() {
        return getHeader();
    }

    @Override
    public void initializeStorage() throws IOException {
        ensureFileExistsWithHeader();
    }

    @Override
    public String[] parseCsvLine(String line) {
        return parseLine(line);
    }

    @Override
    public void appendCsvRow(String row) throws IOException {
        appendRow(row);
    }

    @Override
    public void rewriteCsvRows(List<String> rows) throws IOException {
        rewriteAll(rows);
    }
    
    protected void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) return;

        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);

        Files.writeString(
                csvPath,
                getHeader() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE
        );
    }

    protected void appendRow(String row) throws IOException {
        ensureFileExistsWithHeader();

        boolean needsNewLine = false;

        if (Files.size(csvPath) > 0) {
            try (RandomAccessFile raf = new RandomAccessFile(csvPath.toFile(), "r")) {
                raf.seek(raf.length() - 1);
                char lastChar = (char) raf.readByte();
                if (lastChar != '\n') {
                    needsNewLine = true;
                }
            }
        }

        String content = (needsNewLine ? System.lineSeparator() : "")
                + row
                + System.lineSeparator();

        Files.writeString(
                csvPath,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
        );
    }

    protected void rewriteAll(List<String> rows) throws IOException {
        ensureFileExistsWithHeader();

        StringBuilder content = new StringBuilder();
        content.append(getHeader()).append(System.lineSeparator());

        for (String row : rows) {
            content.append(row).append(System.lineSeparator());
        }

        Files.writeString(
                csvPath,
                content.toString(),
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        );
    }
    
    protected String[] parseLine(String line) {
        List<String> output = new ArrayList<>();
        if (line == null) return output.toArray(new String[0]);
        
        StringBuilder curr = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i=0; i < line.length(); i++) {
            char ch = line.charAt(i);
            
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    curr.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }
            
            if (ch == ',' && !inQuotes) {
                output.add(curr.toString().trim());
                curr.setLength(0);
                continue;
            }
            
            curr.append(ch);
        }
        
        output.add(curr.toString().trim());
        return output.toArray(new String[0]);
    }
}

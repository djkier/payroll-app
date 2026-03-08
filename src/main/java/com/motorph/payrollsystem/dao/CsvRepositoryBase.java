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
import java.util.List;

/**
 *
 * @author djjus
 */
public abstract class CsvRepositoryBase {
    protected final Path csvPath;

    protected CsvRepositoryBase(Path csvPath) {
        this.csvPath = csvPath;
    }

    protected abstract String getHeader();

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
}

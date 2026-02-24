/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author djjus
 */
public class DataBootstrap {
    private DataBootstrap() {}
    
    public static void ensureCsvExported(String resourcePath, Path target) throws IOException{
        ensureCsvExported(resourcePath, target.toString());
    }
    
    public static void ensureCsvExported(String resourcePath, String targetPath) throws IOException {
        Path target = Paths.get(targetPath);
        Path parent = target.getParent();
        
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        
        // If file exist already
        if (Files.exists(target)) return;
        
        //Copy template from resource folder
        try (InputStream is = DataBootstrap.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Missing reousrce template: " + resourcePath); 
            }
            Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
            
        }
    }
}

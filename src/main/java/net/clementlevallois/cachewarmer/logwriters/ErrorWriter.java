/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.logwriters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LEVALLOIS
 */
public class ErrorWriter {

    private static final String ERROR_FILENAME = "errors.log";

    public static boolean writeError(String line) {
        if (line == null || line.isBlank()) {
            ErrorWriter.writeError("error message was null or blank");
            return false;
        }
        try {
            List<String> list = new ArrayList();
            String formattedDateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String timeStampedErrorMessage = formattedDateTime + ": " + line;
            list.add(timeStampedErrorMessage);
            Files.write(Path.of(ERROR_FILENAME), list, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            return true;
        } catch (IOException ex) {
            System.out.println("----");
            System.out.println("could not write this msg to error file:");
            System.out.println(line);
            System.out.println("----");
            return false;
        }
    }
    
    public static String getErrorLogFileName(){
        return ERROR_FILENAME;
    }

}

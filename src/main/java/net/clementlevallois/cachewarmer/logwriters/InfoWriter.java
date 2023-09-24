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
public class InfoWriter {

    private static final String INFO_FILENAME = "info.log";

    public static boolean writeInfo(String line) {
        try {
            if (line == null || line.isBlank()) {
                ErrorWriter.writeError("info message was null or blank");
                return false;
            }
            List<String> list = new ArrayList();
            list.add(line);
            Files.write(Path.of(INFO_FILENAME), list, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            return true;
        } catch (IOException ex) {
            ErrorWriter.writeError("could not write this info message to info log: " + line);
            return false;
        }
    }

    public static String getInfoLogFileName() {
        return INFO_FILENAME;
    }

}

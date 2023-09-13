/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.controller;

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
        try {
            List<String> list = new ArrayList();
            list.add(line);
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

}

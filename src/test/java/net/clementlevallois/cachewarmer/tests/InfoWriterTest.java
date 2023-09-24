/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.tests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;
import net.clementlevallois.cachewarmer.logwriters.InfoWriter;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author LEVALLOIS
 */
public class InfoWriterTest {

    @BeforeEach
    public void deleteLogs() throws IOException{
        Files.deleteIfExists(Path.of(InfoWriter.getInfoLogFileName()));
        Files.deleteIfExists(Path.of(ErrorWriter.getErrorLogFileName()));
    }
    
    @Test
    public void goodMessage() throws IOException {
        InfoWriter.writeInfo("example");
        String readString = Files.readString(Path.of(InfoWriter.getInfoLogFileName()), StandardCharsets.UTF_8);
        assertThat(readString).contains("example");
    }


    @Test
    public void badMessage() throws IOException {
        InfoWriter.writeInfo(null);
        String readStringError = Files.readString(Path.of(ErrorWriter.getErrorLogFileName()), StandardCharsets.UTF_8);
        assertThat(Path.of(InfoWriter.getInfoLogFileName())).doesNotExist();
        assertThat(readStringError).contains("info");
    }

}

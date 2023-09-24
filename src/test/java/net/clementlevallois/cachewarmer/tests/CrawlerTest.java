/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.tests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.clementlevallois.cachewarmer.crawler.Crawler;
import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;
import net.clementlevallois.cachewarmer.logwriters.InfoWriter;
import net.clementlevallois.cachewarmer.pagevisitors.PageVisitorInterface;
import net.clementlevallois.cachewarmer.pagevisitors.PageVisitorWithUrlConnect;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author LEVALLOIS
 */
public class CrawlerTest {

    @BeforeEach
    public void deleteLogs() throws IOException{
        Files.deleteIfExists(Path.of(InfoWriter.getInfoLogFileName()));
        Files.deleteIfExists(Path.of(ErrorWriter.getErrorLogFileName()));
    }
    
    @Test
    public void initOK(){
        PageVisitorInterface pageVisitorWithUrlConnect = new PageVisitorWithUrlConnect();
        assertThat(pageVisitorWithUrlConnect).isNotNull();
        Crawler c = new Crawler(pageVisitorWithUrlConnect);
        assertThat(c).isNotNull();
    }
    
}

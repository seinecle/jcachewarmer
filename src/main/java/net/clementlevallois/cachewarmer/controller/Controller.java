/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import net.clementlevallois.cachewarmer.crawler.Crawler;
import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;
import net.clementlevallois.cachewarmer.logwriters.InfoWriter;
import net.clementlevallois.cachewarmer.pagevisitors.PageVisitorWithUrlConnect;
import net.clementlevallois.cachewarmer.properties.CacheWarmerParameters;

/**
 *
 * @author LEVALLOIS
 */
public class Controller {

    public Controller() {
    }

    public static void main(String args[]) {
        try {
            Files.deleteIfExists(Path.of(InfoWriter.getInfoLogFileName()));
            Files.deleteIfExists(Path.of(ErrorWriter.getErrorLogFileName()));
            new Controller().launchWarmer();
        } catch (IOException ex) {
            ErrorWriter.writeError("error in the main method when deleting log files");
        }
    }

    public void launchWarmer() {

        while (true) {
            try {
                CacheWarmerParameters params = new CacheWarmerParameters();
                Crawler crawler = new Crawler(new PageVisitorWithUrlConnect());
                Clock clock = new Clock("duration of the cache warmup");
                crawler.startCrawl();
                clock.closeAndPrintClock();
                long delayInMinute = CacheWarmerParameters.getIntervalBewtweenTwoCacheWarmupsInMinutes();
                Thread.sleep(Duration.ofMinutes(delayInMinute).toMillis());
            } catch (InterruptedException ex) {
                ErrorWriter.writeError("the waiting operation between two cache warmups has failed");
            }
        }
    }
}

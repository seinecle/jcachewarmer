/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;

/**
 *
 * @author LEVALLOIS
 */
public class CacheWarmerParameters {

    private final String siteMapsResourceFile = "site-map-urls.txt";
    private final String propertiesFileName = "cache-warmer.properties";
    private static Map<String, List<String>> siteMapsAndTheirPages;
    private static List<String> urlsOfsiteMaps;
    private static Properties properties;

    public CacheWarmerParameters() {
        urlsOfsiteMaps = loadFreshListOfXMLSiteUrls();
        listAndStorePageUrlsForAllXMLSiteMaps();
        loadCacheWarmerProperties();
    }

    private List<String> loadFreshListOfXMLSiteUrls() {
        try {
            List<String> lines = Files.readAllLines(Path.of(siteMapsResourceFile), StandardCharsets.UTF_8);
            return lines;
        } catch (IOException ex) {
            ErrorWriter.writeError("could not open file " + siteMapsResourceFile);
            ErrorWriter.writeError(ex.toString());

        }
        return new ArrayList();
    }

    private void listAndStorePageUrlsForAllXMLSiteMaps() {
        siteMapsAndTheirPages = new HashMap();
        for (String urlOfsiteMap : urlsOfsiteMaps) {
            if (urlOfsiteMap.isBlank() || !urlOfsiteMap.startsWith("http")) {
                continue;
            }
            try {
                URL url = new URL(urlOfsiteMap.trim());
                List<String> pages;
                try (InputStream in = url.openStream()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    pages = new ArrayList();
                    List<String> lines = br.lines().toList();
                    for (String line : lines) {
                        if (line.contains("<loc>") || line.contains("<image:loc>") ) {
                            line = line.replace("<loc>", "");
                            line = line.replace("</loc>", "");
                            line = line.replace("<image:loc>", "");
                            line = line.replace("</image:loc>", "");
                            pages.add(line);
                        }
                    }
                }
                siteMapsAndTheirPages.put(urlOfsiteMap, pages);
            } catch (MalformedURLException ex) {
                ErrorWriter.writeError("could not open url of site map " + urlOfsiteMap);
                ErrorWriter.writeError(ex.toString());
            } catch (IOException ex) {
                ErrorWriter.writeError("could not open url of site map " + urlOfsiteMap);
                ErrorWriter.writeError(ex.toString());
            }
        }
    }

    private void loadCacheWarmerProperties() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(propertiesFileName));

        } catch (IOException ex) {
            ErrorWriter.writeError("could not load properties file " + propertiesFileName);
            ErrorWriter.writeError(ex.toString());
        }
    }

    public static Map<String, List<String>> getSiteMapsAndTheirPages() {
        return siteMapsAndTheirPages;
    }

    public static List<String> getUrlsOfsiteMaps() {
        return urlsOfsiteMaps;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getUserAgentMobile() {
        String uaMobile = properties.getProperty("user-agent-mobile");
        if (uaMobile == null || uaMobile.isBlank()) {
            ErrorWriter.writeError("user agent for mobile not found in properties file");
            return "user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 9_5_6; like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Chrome/50.0.1375.298 Mobile Safari/602.4";
        } else {
            return uaMobile;
        }
    }

    public static String getUserAgentDesktop() {
        String uaDesktop = properties.getProperty("user-agent-desktop");
        if (uaDesktop == null || uaDesktop.isBlank()) {
            ErrorWriter.writeError("user agent for desktop not found in properties file");
            return "user-agent=Mozilla/5.0 (compatible; MSIE 11.0; Windows NT 6.3; Win64; x64; en-US Trident/7.0)";
        } else {
            return uaDesktop;
        }
    }

    public static Integer getIntervalInMillisecondsBetweenTwoBatchesOfCallsToUrls() {
        String interval = properties.getProperty("interval-between-two-batches-in-milliseconds");
        if (interval == null || interval.isBlank()) {
            ErrorWriter.writeError("property value interval-between-two-batches-in-milliseconds not found");
            return 0;
        }
        boolean isWellFormattedInteger = interval.trim().matches("\\d+");
        if (!isWellFormattedInteger) {
            ErrorWriter.writeError("property value interval-between-two-batches-in-milliseconds is not a properly formatted integer");
            return 0;
        }
        int intervalMillis = Integer.parseInt(interval.trim());
        return intervalMillis;
    }

    public static Integer getPageLoadingTimeOutInMilliseconds() {
        String timeout = properties.getProperty("page-loading-timeout-in-milliseconds");
        if (timeout == null || timeout.isBlank()) {
            ErrorWriter.writeError("property value page-loading-timeout-in-milliseconds not found");
            return 5_000;
        }
        boolean isWellFormattedInteger = timeout.trim().matches("\\d+");
        if (!isWellFormattedInteger) {
            ErrorWriter.writeError("property value page-loading-timeout-in-milliseconds is not a properly formatted integer");
            return 5_000;
        }
        int timoutInMilliSeconds = Integer.parseInt(timeout.trim());
        return timoutInMilliSeconds;
    }

    public static Integer getNumberOfPagesVisitedConcurrentlyInOneBatch() {
        String numberPageConcurrent = properties.getProperty("number-of-pages-visited-concurrently-in-one-batch");
        if (numberPageConcurrent == null || numberPageConcurrent.isBlank()) {
            ErrorWriter.writeError("property value number-of-pages-visited-concurrently-in-one-batch not found");
            return 5;
        }
        boolean isWellFormattedInteger = numberPageConcurrent.trim().matches("\\d+");
        if (!isWellFormattedInteger) {
            ErrorWriter.writeError("property value number-of-pages-visited-concurrently-in-one-batch is not a properly formatted integer");
            return 5;
        }
        int numberPageConcurrentAsInt = Integer.parseInt(numberPageConcurrent.trim());
        return numberPageConcurrentAsInt;
    }

    public static Integer getIntervalBewtweenTwoCacheWarmupsInMinutes() {
        String delayBetweenTwoCacheWarmupsInMinutes = properties.getProperty("interval-between-two-cache-warmup-in-minutes");
        if (delayBetweenTwoCacheWarmupsInMinutes == null || delayBetweenTwoCacheWarmupsInMinutes.isBlank()) {
            ErrorWriter.writeError("property value interval-between-two-cache-warmup-in-minutes not found");
            return 30;
        }
        boolean isWellFormattedInteger = delayBetweenTwoCacheWarmupsInMinutes.trim().matches("\\d+");
        if (!isWellFormattedInteger) {
            ErrorWriter.writeError("property value interval-between-two-cache-warmup-in-minutes is not a properly formatted integer");
            return 30;
        }
        int delayBetweenTwoCacheWarmupsInMinutesAsInt = Integer.parseInt(delayBetweenTwoCacheWarmupsInMinutes.trim());
        return delayBetweenTwoCacheWarmupsInMinutesAsInt;
    }
}

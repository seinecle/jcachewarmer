/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LEVALLOIS
 */
public class CacheWarmer {

    private final String siteMapsResourceFile = "list of xml site files.txt";
    private Map<String, List<String>> siteMapsAndTheirPages;
    private List<String> urlsOfsiteMaps;
    private List<Crawler> crawlers;

    public CacheWarmer() {
        siteMapsAndTheirPages = new HashMap();
        urlsOfsiteMaps = new ArrayList();
        crawlers = new ArrayList();
    }

    public List<String> loadFreshListOfXMLSiteUrls() {
        try {
            URL resourceXMLSites = CacheWarmer.class.getClassLoader().getResource(siteMapsResourceFile);
            List<String> lines = Files.readAllLines(Path.of(resourceXMLSites.toURI()), StandardCharsets.UTF_8);
            return lines;
        } catch (URISyntaxException | IOException ex) {
            ErrorWriter.writeError(ex.toString());

        }
        return new ArrayList();
    }

    public void listAndStorePageUrlsForAllXMLSiteMaps() {
        for (String urlOfsiteMap : urlsOfsiteMaps) {
            try {
                URL url = new URL(urlOfsiteMap);
                InputStream in = url.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                List<String> pages = new ArrayList();
                List<String> lines = br.lines().toList();
                for (String line : lines) {
                    line = line.replace("<loc>", "");
                    line = line.replace("</loc>", "");
                    pages.add(line);
                }
                siteMapsAndTheirPages.put(urlOfsiteMap, pages);
            } catch (MalformedURLException ex) {
                ErrorWriter.writeError(ex.toString());
            } catch (IOException ex) {
                ErrorWriter.writeError(ex.toString());
            }
        }
    }

    public void createCrawlers() {
        Crawler crawlerMobile = new Crawler(Crawler.Device.MOBILE);
        Crawler crawlerDesktop = new Crawler(Crawler.Device.DESKTOP);
        crawlers.add(crawlerMobile);
        crawlers.add(crawlerDesktop);
    }

    public static void main(String args[]) {
        new CacheWarmer().launchWarmer();
    }

    public void launchWarmer() {
        loadFreshListOfXMLSiteUrls();
        listAndStorePageUrlsForAllXMLSiteMaps();
        createCrawlers();

    }
}

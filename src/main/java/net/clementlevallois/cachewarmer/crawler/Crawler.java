/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.crawler;

import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.clementlevallois.cachewarmer.logwriters.InfoWriter;
import net.clementlevallois.cachewarmer.pagevisitors.PageVisitorInterface;
import net.clementlevallois.cachewarmer.properties.CacheWarmerParameters;

/**
 *
 * @author LEVALLOIS
 */
public class Crawler {

    private final PageVisitorInterface pageVisitor;

    public Crawler(PageVisitorInterface pageVisitor) {
        this.pageVisitor = pageVisitor;
    }

    public void startCrawl() {
        Map<String, List<String>> siteMapsAndTheirPages = CacheWarmerParameters.getSiteMapsAndTheirPages();
        Iterator<Map.Entry<String, List<String>>> iteratorSiteMaps = siteMapsAndTheirPages.entrySet().iterator();
        while (iteratorSiteMaps.hasNext()) {
            Map.Entry<String, List<String>> nextSiteMap = iteratorSiteMaps.next();
            Runnable myRunnable = () -> visitPages(nextSiteMap.getKey(), nextSiteMap.getValue());
            myRunnable.run();
        }
    }

    private void visitPages(String siteMap, List<String> urls) {
        int totalURLs = urls.size();
        int batchSize = CacheWarmerParameters.getNumberOfPagesVisitedConcurrentlyInOneBatch();
        int batchDelayInMillis = CacheWarmerParameters.getIntervalInMillisecondsBetweenTwoBatchesOfCallsToUrls();
        int timeOutForOnePageInMillis = CacheWarmerParameters.getPageLoadingTimeOutInMilliseconds();

        InfoWriter.writeInfo("starting the crawl of this sitemap: " + siteMap);
        InfoWriter.writeInfo("the sitemap comprises " + totalURLs + " urls to visit");
        InfoWriter.writeInfo("params for this cache warming are:");
        InfoWriter.writeInfo("number of pages visited at the same time in a batch: " + batchSize);
        InfoWriter.writeInfo("interval between two batches of pages visits, in milliseconds: " + batchDelayInMillis);
        InfoWriter.writeInfo("time out for page loads, in milliseconds: " + timeOutForOnePageInMillis);

        ExecutorService executor = Executors.newFixedThreadPool(batchSize);
        
        for (int i = 0; i < totalURLs; i += batchSize) {
            List<String> batch = urls.subList(i, Math.min(i + batchSize, totalURLs));
            List<CompletableFuture<Void>> batchFutures = new ArrayList();

            for (String url : batch) {
                CompletableFuture<Void> urlFuture = CompletableFuture.runAsync(() -> {
                    boolean hasPageLoadedCorrectly = pageVisitor.visitOnePageMultipleUserAgents(url, timeOutForOnePageInMillis);
                    if (!hasPageLoadedCorrectly) {
                        ErrorWriter.writeError("this url could not be visited: " + hasPageLoadedCorrectly);
                    }

                }, executor);
                batchFutures.add(urlFuture);
            }

            CompletableFuture<Void>[] batchFutureArray = batchFutures.toArray(CompletableFuture[]::new);
            CompletableFuture<Void> allOf = CompletableFuture.allOf(batchFutureArray);

            try {
                allOf.get(); // Wait for all URLs in the batch to complete
            } catch (InterruptedException | ExecutionException e) {
                ErrorWriter.writeError("error in a batch of urls");
                ErrorWriter.writeError("exception message: " + e.toString());
            }

            try {
                // Wait for the specified delay between batches
                TimeUnit.MILLISECONDS.sleep(batchDelayInMillis);
            } catch (InterruptedException e) {
                ErrorWriter.writeError("error when waiting between two batches");
                ErrorWriter.writeError("exception message: " + e.toString());
            }
        }
        InfoWriter.writeInfo("all urls have been visited for this site map: " + siteMap);
        executor.shutdown();
    }
}

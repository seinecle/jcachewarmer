/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.pagevisitors;

/**
 *
 * @author LEVALLOIS
 */
public interface PageVisitorInterface {
    
    public boolean visitOnePage(String url, String userAgent, int timeOutInMillis);

    public boolean visitOnePage(String url, int timeOutInMillis);
    
    public boolean visitOnePageMultipleUserAgents(String url, int timeOutInMillis);
    
}

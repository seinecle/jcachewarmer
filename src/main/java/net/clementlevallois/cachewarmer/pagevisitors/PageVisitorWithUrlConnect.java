/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.pagevisitors;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import net.clementlevallois.cachewarmer.logwriters.ErrorWriter;
import net.clementlevallois.cachewarmer.properties.CacheWarmerParameters;

/**
 *
 * @author LEVALLOIS
 */
public class PageVisitorWithUrlConnect implements PageVisitorInterface {

    @Override
    public boolean visitOnePage(String url, String userAgent, int timeOutInMillis) {
        try {
            // Specify the URL you want to open
            URL urlObject = new URL(url);

            // Open a connection to the URL
            URLConnection connection = urlObject.openConnection();

            // Set the User-Agent header
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setUseCaches(false);

            // Set the connection timeout
            connection.setConnectTimeout(timeOutInMillis);

            // Attempt to open the connection
            connection.connect();

            // If we reach this point without an exception, the connection was successful
            return true;
        } catch (IOException e) {
            // Handle exceptions, such as timeouts or other connection errors
            ErrorWriter.writeError("error visiting this url: " + url);
            ErrorWriter.writeError("error trace: " + e.toString());
            return false; // Connection was not successful
        }
    }

    @Override
    public boolean visitOnePage(String url, int timeOutInMillis) {
        try {
            // Specify the URL you want to open
            URL urlObject = new URL(url);

            // Open a connection to the URL
            URLConnection connection = urlObject.openConnection();

            // Set the connection timeout
            connection.setConnectTimeout(timeOutInMillis);
            connection.setUseCaches(false);

            // Attempt to open the connection
            connection.connect();

            // If we reach this point without an exception, the connection was successful
            return true;
        } catch (IOException e) {
            ErrorWriter.writeError("error visiting this url: " + url);
            ErrorWriter.writeError("error trace: " + e.toString());
            return false; // Connection was not successful
        }
    }

    @Override
    public boolean visitOnePageMultipleUserAgents(String url, int timeOutInMillis) {
        try {
            // Specify the URL you want to open
            URL urlObject = new URL(url);

            // Open a connection to the URL
            URLConnection connection = urlObject.openConnection();

            // Set the connection timeout
            connection.setConnectTimeout(timeOutInMillis);
            connection.setUseCaches(false);

            // Set the user agent
            connection.setRequestProperty("User-Agent", CacheWarmerParameters.getUserAgentDesktop());

            // Attempt to open the connection
            connection.connect();

            int contentLength = connection.getContentLength();
            

            // Set the connection timeout
            connection = urlObject.openConnection();
            connection.setConnectTimeout(timeOutInMillis);
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", CacheWarmerParameters.getUserAgentMobile());

            // Attempt to open the connection
            connection.connect();

            contentLength = connection.getContentLength();

            // If we reach this point without an exception, the connection was successful
            return true;
        } catch (IOException e) {
            ErrorWriter.writeError("error visiting this url: " + url);
            ErrorWriter.writeError("error trace: " + e.toString());
            return false; // Connection was not successful
        }
    }

}

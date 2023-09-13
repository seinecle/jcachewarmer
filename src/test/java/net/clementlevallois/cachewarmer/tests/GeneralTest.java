/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.tests;

import java.util.List;
import net.clementlevallois.cachewarmer.controller.CacheWarmer;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author LEVALLOIS
 */
public class GeneralTest {

    @Test
    public void readTextFileContainingSiteXMLUrls() {
        CacheWarmer cw = new CacheWarmer();
        List<String> freshListOfXMLSiteUrls = cw.loadFreshListOfXMLSiteUrls();
        Assert.assertEquals(1d, freshListOfXMLSiteUrls.size(), 0d);
    }

    @Test
    public void visitOnePageWithSelenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        String title = driver.getTitle();
        Assert.assertEquals("title of the web page should be correct", "Web form", title);
    }

}

/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.cachewarmer.tests;

import net.clementlevallois.cachewarmer.properties.CacheWarmerParameters;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;


/**
 *
 * @author LEVALLOIS
 */
public class CacheWarmerParametersTest {

    @Test
    public void properInitalizationOfParams() {
        CacheWarmerParameters cwp = new CacheWarmerParameters();
        assertThat(CacheWarmerParameters.getProperties()).isNotNull();
        assertThat(CacheWarmerParameters.getSiteMapsAndTheirPages()).isNotNull();
        assertThat(CacheWarmerParameters.getUrlsOfsiteMaps()).isNotNull();
    }


    @Test
    public void defaultParamsExist() {
        CacheWarmerParameters cwp = new CacheWarmerParameters();
        assertThat(CacheWarmerParameters.getIntervalBewtweenTwoCacheWarmupsInMinutes()).isGreaterThan(0);
        assertThat(CacheWarmerParameters.getNumberOfPagesVisitedConcurrentlyInOneBatch()).isGreaterThan(0);
        assertThat(CacheWarmerParameters.getPageLoadingTimeOutInMilliseconds()).isGreaterThan(500);
        assertThat(CacheWarmerParameters.getUserAgentDesktop()).startsWith("user");
        assertThat(CacheWarmerParameters.getUserAgentMobile()).startsWith("user");
    }

}

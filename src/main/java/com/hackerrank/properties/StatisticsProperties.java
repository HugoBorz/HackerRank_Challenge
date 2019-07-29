package com.n26.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties ("stat")
public class StatisticsProperties {

    @Value("${stat.stat-interval-sec:60}")
    private int statisticsIntervalSec;

    @Value("${stat.cache-life-time-sec:120}")
    private int cacheLifeTimeSec;

    @Value("${stat.cache-added-count-before-clean:100}")
    private int addedCountBeforeClean;

    public int getAddedCountBeforeClean() {
        return addedCountBeforeClean;
    }

    public int getCacheLifeTimeSec() {
        return cacheLifeTimeSec;
    }

    public int getStatisticsIntervalSec() {
        return statisticsIntervalSec;
    }

    public void setAddedCountBeforeClean(int addedCountBeforeClean) {
        this.addedCountBeforeClean = addedCountBeforeClean;
    }

    public StatisticsProperties setCacheLifeTimeSec(int cacheLifeTimeSec) {
        this.cacheLifeTimeSec = cacheLifeTimeSec;
        return this;
    }

    public StatisticsProperties setStatisticsIntervalSec(int statisticsIntervalSec) {
        this.statisticsIntervalSec = statisticsIntervalSec;
        return this;
    }

    @Override
    public String toString()
    {
        return "StatisticsProperties{" +
                "statisticsIntervalSec = " + statisticsIntervalSec +
                ", cacheLifeTimeSec = " + cacheLifeTimeSec +
                ", addedCountBeforeClean = " + addedCountBeforeClean +
                "}";
    }


}

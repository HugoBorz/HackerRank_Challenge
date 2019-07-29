package com.hackerrank.data.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hackerrank.data.Statistics;
import com.hackerrank.data.Transaction;
import com.hackerrank.properties.StatisticsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component("transactionsCache")
public class TransactionsCache implements CacheEngine{

    
    @Autowired
    StatisticsProperties properties;
    
    //using Guava Cache for time-limited storage
    private Cache<Long, AbstractMap.SimpleEntry<Long, BigDecimal>> expiredCache;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ConcurrentMap<Long, AbstractMap.SimpleEntry<Long, BigDecimal>> mapCache;
    private int addedCount = 0;
    private long tKey = 0;
    
    @PostConstruct
    public void postConstruct()
    {

        expiredCache = CacheBuilder.newBuilder().expireAfterWrite(properties.getCacheLifeTimeSec(), TimeUnit.SECONDS).build();
        mapCache = expiredCache.asMap();
        log.debug("TransactionCache was created successfully!");
    }

    @Override
    public void put(Transaction t) {
        tKey++;
        mapCache.put(tKey, new AbstractMap.SimpleEntry<>(t.getTimestamp(), t.getAmount()));
        log.debug("Transaction added to cache. Cache lenght: "+mapCache.size());
        addedCount++;
        if (addedCount > properties.getAddedCountBeforeClean()){
            cleanCache();
            addedCount = 0;
        }
        
    }

    @Override
    public Statistics getStatistic() {
        Instant currentMoment = Instant.now();
        long fromMoment = currentMoment.minusMillis(properties.getStatisticsIntervalSec()*1000).toEpochMilli();
        log.debug(String.format("Get statistic from %d to %d. Cache size: %d", fromMoment, currentMoment.toEpochMilli(),mapCache.size()));
        return calculateStatistics(fromMoment, currentMoment.toEpochMilli());
    }
    
    private void cleanCache(){
        //clean old data
        expiredCache.cleanUp();
        log.debug("Cache size after cleanUp: " + mapCache.size());
    }

    private Statistics calculateStatistics(long fromMoment, long toMoment)
    {
        cleanCache();
        
        double max = 0, min = 0, sum = 0;
        long count = 0;

        for (Map.Entry<Long, AbstractMap.SimpleEntry<Long, BigDecimal>> t : mapCache.entrySet() )
        {
            long timestamp = t.getValue().getKey();
            if (timestamp < fromMoment || timestamp > toMoment){
                continue;
            }

            double amount = t.getValue().getValue().doubleValue();

            if (count == 0){
                min = amount;
                max = amount;
            }
            else if (amount < min)
            {
                min = amount;
            }
            else if (amount > max)
            {
                max = amount;
            }

            sum += amount;
            count++;
        }
        return new Statistics(sum, count == 0 ? 0 : sum/count, max, min, count);
    }

    @Override
    public void clear() {
        mapCache.clear();
    }
}

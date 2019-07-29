package com.hackerrank.service;

import com.hackerrank.common.NoContentException;
import com.hackerrank.common.UnprocessableException;
import com.hackerrank.data.Statistics;
import com.hackerrank.data.Transaction;
import com.hackerrank.data.cache.CacheEngine;
import com.hackerrank.data.cache.TransactionsCache;
import com.hackerrank.properties.StatisticsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private StatisticsProperties properties;
    @Autowired
    private final CacheEngine cache = new TransactionsCache();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void addTransaction(Transaction t) throws NoContentException, IllegalArgumentException, UnprocessableException {

        checkTransaction(t);
        cache.put(t);
    }

    private void checkTransaction(Transaction t) throws NoContentException, IllegalArgumentException, UnprocessableException {
        long timestamp;
        if (t == null || t.getTimestamp() == 0)
        {
            log.debug("Invalid transaction");
            throw new IllegalArgumentException("Invalid transaction");
        }

        timestamp = t.getTimestamp();
        Instant currentMoment = Instant.now();

        if (timestamp > currentMoment.toEpochMilli()) {
            log.debug(String.format("Transaction from the future. Current: %d. Transaction: %d.", currentMoment.toEpochMilli(), timestamp));
            throw new UnprocessableException("Transaction from the future.");
        }

        if (timestamp < currentMoment.minusMillis(properties.getStatisticsIntervalSec()*1000).toEpochMilli())
        {
            log.debug(String.format("Transaction date is too old. Current: %d. Transaction: %d. StatisticInterval %d ms", currentMoment.toEpochMilli(), timestamp,properties.getStatisticsIntervalSec()*1000));
            throw new NoContentException("Transaction date is too old");
        }
        log.debug(String.format("Transaction is correct. Current: %d. Transaction: %d.", currentMoment.toEpochMilli(), timestamp));
    }

    @Override
    public Statistics calculateStatistic() {

        log.debug("Calculate statistics in service");
        return cache.getStatistic();
    }

    @Override
    public void deleteTransactions() {
        cache.clear();
    }
}

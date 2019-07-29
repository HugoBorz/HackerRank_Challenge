package com.hackerrank.data.cache;

import com.hackerrank.data.Statistics;
import com.hackerrank.data.Transaction;

public interface CacheEngine {

    void put (Transaction t);
    Statistics getStatistic();
    void clear();
}

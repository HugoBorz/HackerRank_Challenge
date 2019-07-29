package com.n26.data.cache;

import com.n26.data.Statistics;
import com.n26.data.Transaction;

public interface CacheEngine {

    void put (Transaction t);
    Statistics getStatistic();
    void clear();
}

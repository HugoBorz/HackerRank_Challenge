package com.n26.service;

import com.n26.common.NoContentException;
import com.n26.common.UnprocessableException;
import com.n26.data.Statistics;
import com.n26.data.Transaction;

public interface TransactionService {

    void addTransaction(Transaction transaction) throws NoContentException, UnprocessableException;

    Statistics calculateStatistic();

    void deleteTransactions();

}

package com.hackerrank.service;

import com.hackerrank.common.NoContentException;
import com.hackerrank.common.UnprocessableException;
import com.hackerrank.data.Statistics;
import com.hackerrank.data.Transaction;

public interface TransactionService {

    void addTransaction(Transaction transaction) throws NoContentException, UnprocessableException;

    Statistics calculateStatistic();

    void deleteTransactions();

}

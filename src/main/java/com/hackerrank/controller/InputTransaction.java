package com.n26.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class InputTransaction {

    private final String amount;
    private final String timestamp;

    InputTransaction(String amount, String timestamp)
    {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }


}

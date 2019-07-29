package com.hackerrank.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class InputTransaction {

    @JsonProperty("amount")
    private final String amount;
    @JsonProperty("timestamp")
    private final String timestamp;

    public InputTransaction(String amount, String timestamp)
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

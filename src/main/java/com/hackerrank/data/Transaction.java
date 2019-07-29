package com.hackerrank.data;

import com.hackerrank.common.UnprocessableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;


public class Transaction {

    private final BigDecimal amount;
    private final long timestamp;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public Transaction(String amount, String timestamp) throws UnprocessableException {
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));

        try {
            this.amount = new BigDecimal(amount);
        }
        catch (NumberFormatException ex)
        {
            log.debug(String.format("NumberFormatException for amount '%s'", amount));
            throw new UnprocessableException(ex.getMessage());
        }

        try {
            this.timestamp = sdf.parse(timestamp, new ParsePosition(0)).getTime();
        }
        catch (NullPointerException ex)
        {
            log.debug(String.format("NullPointerException for timestamp '%s'", timestamp));
            throw new UnprocessableException(ex.getMessage());
        }
        log.debug("Create transaction: "+ this.toString());
    }

    public BigDecimal getAmount() {
        return amount;
    }


    public long getTimestamp() { return timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Transaction that = (Transaction) o;

        if (timestamp != that.timestamp)
            return false;
        
        return  that.amount.compareTo(amount) == 0;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        temp = Double.doubleToLongBits(amount.doubleValue());
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    @Override
    public String toString() {
         return String.format("{\"amount\": %s, \"timestamp\": %s}", amount, sdf.format(timestamp));
    }
}

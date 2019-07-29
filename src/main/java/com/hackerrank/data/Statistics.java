package com.n26.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Statistics {

    //we need jsonformat because of loss of last zeros in default Json serializer format
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal sum;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal avg;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal max;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal min;
    
    private long count;
    RoundingMode rMode = RoundingMode.HALF_UP;

    public Statistics(double sum, double avg, double max, double min, long count)
    {
        this.sum = BigDecimal.valueOf(sum).setScale(2, rMode);
        this.avg = BigDecimal.valueOf(avg).setScale(2, rMode);
        this.max = BigDecimal.valueOf(max).setScale(2, rMode);
        this.min = BigDecimal.valueOf(min).setScale(2, rMode);
        this.count = count;
     
       
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "sum = " + sum +
                ", count = " + count +
                ", avg = " + avg +
                ", min = " + min +
                ", max = " + max +
                "}";
    }
}

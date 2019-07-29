package com.hackerrank;

import com.hackerrank.common.NoContentException;
import com.hackerrank.common.UnprocessableException;
import com.hackerrank.data.Statistics;
import com.hackerrank.data.Transaction;
import com.hackerrank.properties.StatisticsProperties;
import com.hackerrank.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.yml")
public class TransactionServiceTest {

    @Autowired
    TransactionService tService;
    
    @Autowired
    StatisticsProperties properties;

    RoundingMode rMode = RoundingMode.HALF_UP;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    @Before
    public void before(){
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        tService.deleteTransactions();
    }

    @Test
    public void add_Valid_Transaction() throws UnprocessableException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", sdf.format(Date.from(current.minusMillis(1000))));
        tService.addTransaction(t);
        Statistics stat = tService.calculateStatistic();
        assertEquals( 1,stat.getCount());
        assertEquals(t.getAmount(), stat.getMax());
        assertEquals(t.getAmount(), stat.getMin());
        assertEquals(t.getAmount(), stat.getAvg());
        assertEquals(t.getAmount(), stat.getSum());
    }



    @Test(expected = NoContentException.class)
    public void add_Old_Transaction() throws UnprocessableException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", sdf.format(Date.from(current.minusSeconds(1000))));
        tService.addTransaction(t);
    }

    @Test(expected = UnprocessableException.class)
    public void add_Future_Transaction() throws UnprocessableException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", sdf.format(Date.from(current.plusSeconds(1000))));
        tService.addTransaction(t);
    }

    @Test(expected = UnprocessableException.class)
    public void add_Incorrect_Amount_Transaction() throws UnprocessableException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("0xFF", sdf.format(Date.from(current.minusSeconds(1000))));
        tService.addTransaction(t);
    }

    @Test(expected = UnprocessableException.class)
    public void add_Incorrect_Timestamp_Transaction() throws UnprocessableException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", "yyyy-MM-dd'T'HH:mm:ss.SSSX");
        tService.addTransaction(t);
    }

    @Test
    public void delete_Transactions() throws NoContentException, UnprocessableException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", sdf.format(Date.from(current.minusMillis(1000))));
        tService.addTransaction(t);
        tService.deleteTransactions();
        Statistics stat = tService.calculateStatistic();
        assertEquals( 0,stat.getCount());
    }

   @Test
    public void calculate_statistics() throws NoContentException, UnprocessableException {
        Instant current = Instant.now();
        BigDecimal amount = new BigDecimal("10.22");
        BigDecimal minAmount = new BigDecimal("10.21");
        BigDecimal maxAmount = new BigDecimal("10.23");

        BigDecimal sum = new BigDecimal(0);
        int tCount = 10;
        for (int i =0; i<10; i++)
        {
            Transaction t = new Transaction(amount.toString(), sdf.format(Date.from(current.minusMillis(1000-i))));
            tService.addTransaction(t);
            sum = sum.add(amount);
        }
        Transaction tMin = new Transaction(minAmount.toString(), sdf.format(Date.from(current.minusMillis(1001))));
        tService.addTransaction(tMin);
        sum = sum.add(minAmount);
        Transaction tMax= new Transaction(maxAmount.toString(), sdf.format(Date.from(current.minusMillis(1002))));
        tService.addTransaction(tMax);
        sum = sum.add(maxAmount);

        Statistics stat = tService.calculateStatistic();

        assertEquals( tCount + 2,stat.getCount());
        assertEquals(maxAmount, stat.getMax());
        assertEquals(minAmount, stat.getMin());
        assertEquals(amount, stat.getAvg());
        assertEquals(sum, stat.getSum());
    }

    @Test
    public void clearCache() throws UnprocessableException, InterruptedException, NoContentException {
        Instant current = Instant.now();
        Transaction t = new Transaction("10.22", sdf.format(Date.from(current.minusMillis(1000))));
        tService.addTransaction(t);

        Thread.sleep((properties.getCacheLifeTimeSec()+2)*1000);
        Statistics stat = tService.calculateStatistic();
        assertEquals(0,stat.getCount());
    }



}
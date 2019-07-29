package com.n26.controller;

import com.n26.common.NoContentException;
import com.n26.common.UnprocessableException;
import com.n26.data.Transaction;
import com.n26.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class TransactionController {

    @Autowired
    TransactionService tService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/transactions")
    public ResponseEntity addTransaction(@RequestBody InputTransaction t){

        try {
            Transaction newTransaction = new Transaction(t.getAmount(), t.getTimestamp());
            tService.addTransaction(newTransaction);
            //201 – in case of success
            return new ResponseEntity(HttpStatus.CREATED);
        }
        catch(IllegalArgumentException ex)
        {
            //400 – if the JSON is invalid
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        catch(NoContentException ex)
        {
            //204 – if the transaction is older than 60 seconds
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch(UnprocessableException ex)
        {
            //422 – if any of the fields are not parsable or the transaction date is in the future
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }

    @GetMapping("/statistics")
    public ResponseEntity calculateStatistics()
    {
        log.debug("Got calc stat request");
        return
                ResponseEntity.ok().body(tService.calculateStatistic());
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactions()
    {
        //The endpoint should accept an empty request body and return a 204 status code.
        tService.deleteTransactions();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}

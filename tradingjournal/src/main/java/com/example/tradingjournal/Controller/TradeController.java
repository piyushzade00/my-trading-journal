package com.example.tradingjournal.Controller;

import com.example.tradingjournal.DTO.TradeDTO;
import com.example.tradingjournal.Service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @RequestMapping(value = "/viewTrade", method = RequestMethod.GET)
    public  ResponseEntity<TradeDTO> viewTradeByID(@RequestParam("id") String name) {
        Optional<TradeDTO> trade = tradeService.findByName(name);
        if(trade.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(trade.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
   }

    @RequestMapping(value = "/viewAllTrades", method = RequestMethod.GET)
    public ResponseEntity<Iterable<TradeDTO>> viewAllTrades() {
        Iterable<TradeDTO> trades = tradeService.findAll();
        if(trades.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.OK).body(trades);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/saveTrade", method = RequestMethod.POST)
    public ResponseEntity<TradeDTO> saveTrade(@RequestBody TradeDTO trade)
    {
        TradeDTO savedTrade = tradeService.save(trade);
        return ResponseEntity.status(HttpStatus.OK).body(savedTrade);
    }

    @RequestMapping(value = "/deleteTrade", method = RequestMethod.GET)
    public ResponseEntity<String> deleteTradeByID(@RequestParam("username") String name) {
        tradeService.deleteByName(name);
        return ResponseEntity.status(HttpStatus.OK).body("Trade Deleted");
    }
}

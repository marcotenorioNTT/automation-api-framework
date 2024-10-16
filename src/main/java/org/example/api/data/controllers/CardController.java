package org.example.api.data.controllers;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.request.CardRequest;
import org.example.api.service.AccountService;
import org.example.api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.api.data.entity.Customer;
import org.example.api.service.CardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class CardController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    private final CardService card;

    public CardController(CardService card) {
        this.card = card;
    }

    @GetMapping("api/BDcards")     // get all cards from DB
    public List<Card> card() {
        return card.findAll();
    }

    @GetMapping("api/card/{cardId}")       // get 1 card by cardId
    public Optional<Card> card(@PathVariable Integer id) {
        return card.findById(id);
    }

    @GetMapping("api/cards/{accountId}")    // get all cards by accountId
    public List<Card> cardsByAccountId(@PathVariable Integer accountId) {
        return card.findByAccountId(accountId);
    }

    @PostMapping("/api/card/new")
    public ResponseEntity<String> newCard(@RequestBody CardRequest cardRequest) {
        // Verify user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Assuming username is contained in the customerId
        Integer customerId = Integer.valueOf(authentication.getName());

        // If we could not find the customer -> you are not logged
        if(!customerService.findById(customerId).isPresent()){
            return ResponseEntity.badRequest().body("Error creating card: You must be logged");
        }

        // Verifying correct data from user
        if (!cardRequest.getType().equals("Debit") && !cardRequest.getType().equals("Credit")) {
            return ResponseEntity.badRequest().body("Error creating card: card type not valid");
        }

        // Generate randomly CVC and CardNumber
        Random rnd = new Random();
        Long number = rnd.nextLong() % 10000000000000000L;
        int cvc = rnd.nextInt() % 1000;
        // We dont want negative CVC numbers
        if (cvc < 0) cvc += -1;

        // Create new card
        Card newCard = new Card();
        newCard.setType(cardRequest.getType());
        newCard.setExpirationDate(cardRequest.getDate());
        newCard.setNumber(number);
        newCard.setCvc(cvc);

        // Find account that will contain card
        Optional<Account> account = accountService.findById(cardRequest.getAccountId());

        // Verifying correct account data
        if (!account.isPresent()) {
            return ResponseEntity.badRequest().body("Error creating card: account not found");
        }

        newCard.setAccount(account.get());
        card.save(newCard);

        return ResponseEntity.ok("Card created successfully");
    }
    @GetMapping("/api/cards")   // get all cards from a customer
    public List<Card> getCards(@AuthenticationPrincipal Customer userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Assuming username contains customerId
        Integer customerId = Integer.valueOf(authentication.getName());

        return card.getCardsByCustomerId(customerId);

    }
}

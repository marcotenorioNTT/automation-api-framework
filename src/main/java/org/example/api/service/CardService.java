package org.example.api.service;

import org.example.api.data.entity.Card;
import org.example.api.data.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Optional<Card> findById(Integer cardId) {
        return cardRepository.findByCardId(cardId);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void deleteById(Integer cardId) {
        cardRepository.deleteById(cardId);
    }

    public List<Card> findByAccountId(Integer accountId) {
        return cardRepository.findByAccount_AccountId(accountId);
    }
}

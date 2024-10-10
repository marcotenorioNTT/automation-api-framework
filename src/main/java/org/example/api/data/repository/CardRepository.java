package org.example.api.data.repository;

import org.example.api.data.entity.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Optional<Card> findByCardId(Integer cardId);
}
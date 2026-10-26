package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Card;

import java.util.List;

public interface CardDao {
    Card findByAccountId(long accountId);
    Card findById(long cardId);
    long createCard(Card card);
    void updateStatus(long cardId, String status);
    void updateDailyLimit(long cardId, double limit);
    List<Card> findAll();
}

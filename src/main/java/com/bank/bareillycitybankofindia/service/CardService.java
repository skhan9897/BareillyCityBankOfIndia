package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Card;

public interface CardService {
    Card getCardByAccountId(long accountId);
    Card requestNewCard(long accountId) throws Exception;
    void blockCard(long cardId) throws Exception;
    void unblockCard(long cardId) throws Exception;
    void updateDailyLimit(long cardId, double limit) throws Exception;
}

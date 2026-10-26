package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.dao.CardDao;
import com.bank.bareillycitybankofindia.dao.impl.CardDaoImpl;
import com.bank.bareillycitybankofindia.model.Card;
import com.bank.bareillycitybankofindia.security.InputValidator;
import com.bank.bareillycitybankofindia.service.CardService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardServiceImpl implements CardService {

    private final CardDao cardDao = new CardDaoImpl();

    @Override
    public Card getCardByAccountId(long accountId) {
        return cardDao.findByAccountId(accountId);
    }

    @Override
    public Card requestNewCard(long accountId) throws Exception {
        Card existing = cardDao.findByAccountId(accountId);
        if (existing != null && "ACTIVE".equalsIgnoreCase(existing.getStatus())) {
            return existing;
        }

        String rawCard = "4532" + (100000000000L + (long)(Math.random() * 899999999999L));
        LocalDate now = LocalDate.now();

        Card card = new Card();
        card.setAccountId(accountId);
        card.setCardNumberMasked(InputValidator.maskCardNumber(rawCard));
        card.setCardType("DEBIT");
        card.setExpiryMonth(now.getMonthValue());
        card.setExpiryYear(now.getYear() + 5);
        card.setDailyLimit(BigDecimal.valueOf(50000.00));
        card.setStatus("ACTIVE");

        long id = cardDao.createCard(card);
        card.setId(id);
        return card;
    }

    @Override
    public void blockCard(long cardId) throws Exception {
        cardDao.updateStatus(cardId, "BLOCKED");
    }

    @Override
    public void unblockCard(long cardId) throws Exception {
        cardDao.updateStatus(cardId, "ACTIVE");
    }

    @Override
    public void updateDailyLimit(long cardId, double limit) throws Exception {
        if (limit <= 0 || limit > 500000) throw new IllegalArgumentException("Limit must be between ₹1 and ₹5,00,000.");
        cardDao.updateDailyLimit(cardId, limit);
    }
}

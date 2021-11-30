package com.revature.banking.services;

import com.revature.banking.daos.FlashcardDAO;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.Flashcard;

import java.util.List;

public class FlashcardService {

    private final FlashcardDAO cardDAO;

    public FlashcardService(FlashcardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public List<Flashcard> findAllCards() {
        return cardDAO.findAll();
    }

    public List<Flashcard> findMyCards(String ownerId) {

        System.out.println("OwnerId = " + ownerId);

        if (ownerId == null || ownerId.equals("")) {
            throw new InvalidRequestException("Invalid owner id provided!");
        }

        return cardDAO.findCardsByCreatorId(ownerId);

    }

    public void createNewCard(Flashcard newCard) {

        if (!isCardValid(newCard)) {
            throw new InvalidRequestException("Invalid card information values provided!");
        }

        // TODO refactor to use HttpSession
//        newCard.setCreator(userService.getSessionUser());
        Flashcard addedCard = cardDAO.save(newCard);

        if (addedCard == null) {
            throw new ResourcePersistenceException("The card could not be persisted to the datasource!");
        }

    }

    public boolean isCardValid(Flashcard card) {
        if (card == null) return false;
        if (card.getQuestionText() == null || card.getQuestionText().trim().equals("")) return false;
        return (card.getAnswerText() != null && !card.getAnswerText().trim().equals(""));
    }
}

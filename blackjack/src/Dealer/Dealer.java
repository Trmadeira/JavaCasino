package Dealer;

import Cards.Cards;

public class Dealer {

    int random;
    private Cards cards;
    private String suits;

    public int drawCards() {
        random = (int) (Math.random() * Cards.values().length);
        cards = Cards.values()[random];
        suits = cards.getSuits();
        return cards.getValue();
    }

    public String getSuits() {
        return suits;
    }

    public int getCardValue() {
        return cards.getValue();
    }
}

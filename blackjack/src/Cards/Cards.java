package Cards;

public enum Cards {

    CLUBS_A(1, "♣"),
    CLUBS_2(2, "♣"),
    CLUBS_3(3, "♣"),
    CLUBS_4(4, "♣"),
    CLUBS_5(5, "♣"),
    CLUBS_6(6, "♣"),
    CLUBS_7(7, "♣"),
    CLUBS_8(8, "♣"),
    CLUBS_9(9, "♣"),
    CLUBS_10(10, "♣"),
    CLUBS_J(10, "♣"),
    CLUBS_Q(10, "♣"),
    CLUBS_K(10, "♣"),

    DIAMOND_A(1, "♦"),
    DIAMOND_2(2, "♦"),
    DIAMOND_3(3, "♦"),
    DIAMOND_4(4, "♦"),
    DIAMOND_5(5, "♦"),
    DIAMOND_6(6, "♦"),
    DIAMOND_7(7, "♦"),
    DIAMOND_8(8, "♦"),
    DIAMOND_9(9, "♦"),
    DIAMOND_10(10, "♦"),
    DIAMOND_J(10, "♦"),
    DIAMOND_Q(10, "♦"),
    DIAMOND_K(10, "♦"),

    HEART_A(1, "♥"),
    HEART_2(2, "♥"),
    HEART_3(3, "♥"),
    HEART_4(4, "♥"),
    HEART_5(5, "♥"),
    HEART_6(6, "♥"),
    HEART_7(7, "♥"),
    HEART_8(8, "♥"),
    HEART_9(9, "♥"),
    HEART_10(10, "♥"),
    HEART_J(10, "♥"),
    HEART_Q(10, "♥"),
    HEART_K(10, "♥"),

    SPADES_A(1, "♠"),
    SPADES_2(2, "♠"),
    SPADES_3(3, "♠"),
    SPADES_4(4, "♠"),
    SPADES_5(5, "♠"),
    SPADES_6(6, "♠"),
    SPADES_7(7, "♠"),
    SPADES_8(8, "♠"),
    SPADES_9(9, "♠"),
    SPADES_10(10, "♠"),
    SPADES_J(10, "♠"),
    SPADES_Q(10, "♠"),
    SPADES_K(10, "♠");

    int value;
    String suits;

    Cards(int value, String suits) {
        this.value = value;
        this.suits = suits;
    }

    public int getValue() {
        return value;
    }

    public String getSuits() {
        return suits;
    }
}
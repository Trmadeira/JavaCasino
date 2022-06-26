package Games;

import Client.ClientThreadManager;
import Dealer.Dealer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class Blackjack {

    //Colors for text
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";


    //Variables
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private Dealer dealer = new Dealer();

    //Options for blackjack game
    private String[] gameOptions = {"Hit", "Stand"};

    //More variables
    private String message;
    private boolean gameEnd;
    private int gameAnswerIndex;
    private int money;
    private int bet = 0;
    private int dealerCardsValue = 0;
    private int playerCardsValue = 0;
    private ArrayList<String> dealerCards = new ArrayList<>();
    private ArrayList<String> playerCards = new ArrayList<>();
    private String allPlayerCards = "";
    private String allDealerCards = "";
    private boolean dealerFinished;

    //Blackjack constructor receives the socket and prompt, create new input and output streams;
    public Blackjack(Socket clientSocket, Prompt prompt) {
        this.prompt = prompt;
        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error creating in/out streams");
        }
    }

    public void run(int money) {
        this.money = money;
        game();
    }

    public void game() {
        dealerFinished = false;
        startGame();
        giveDealerOneCard();
        out.print(showDealerCards());

        givePlayerCards(2);
        out.print(showPlayerCards());

        checkPlayerJackpot();
        checkDealerJackpot();

        while (!gameEnd) {
            askPlayerAction();
            checkPlayerJackpot();
            checkDealerJackpot();

            switch (gameAnswerIndex) {
                case 1: {
                    givePlayerCards(1);
                    checkPlayerJackpot();
                    checkDealerJackpot();
                    out.print(showDealerCards());
                    out.print(showPlayerCards());
                    break;
                }
                case 2: {
                    giveDealerOneCard();
                    checkPlayerJackpot();
                    checkDealerJackpot();
                    giveDealerCards();
                    out.print(showDealerCards());
                    out.print(showPlayerCards());
                    checkPlayerJackpot();
                    checkDealerJackpot();
                    break;
                }
            }

        }
    }

    public String showPlayerCards() {
        allPlayerCards = "";
        for (String playerCards : playerCards) {
            allPlayerCards += playerCards;
        }

        return "\n" + message + " : " + allPlayerCards + "\n";
    }

    public String showDealerCards() {
        allDealerCards = "";
        for (String dealerCards : dealerCards) {
            allDealerCards += dealerCards;
        }
        return "\nDEALER : " + allDealerCards + "\n";
    }

    public void givePlayerCards(int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            playerCardsValue += dealer.drawCards();
            playerCards.add(dealer.getCardValue() + dealer.getSuits());
        }
    }

    public void giveDealerOneCard() {
        dealerCardsValue += dealer.drawCards();
        dealerCards.add(dealer.getCardValue() + dealer.getSuits());
    }

    public void giveDealerCards() {
        if (dealerCardsValue >= 17) {
            out.print("Dealer is standing");
            dealerFinished = true;
        } else {
            while (dealerCardsValue <= 17) {
                dealerCardsValue += dealer.drawCards();
                dealerCards.add(dealer.getCardValue() + dealer.getSuits());
                dealerFinished = true;
            }
        }
    }

    public void askPlayerAction() {
        MenuInputScanner scanner = new MenuInputScanner(gameOptions);
        out.print("\nWhat do you want to do? \n");
        gameAnswerIndex = prompt.getUserInput(scanner);
    }

    public void checkPlayerJackpot() {
        if (playerCardsValue == 21) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_GREEN + "\n" + message + " has hit a jackpot! He wins: " + bet * 2 + "€\n" + ANSI_RESET);
            money += bet;
        } else if (playerCardsValue > 21) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_RED + "\n" + message + " busted! He lost: " + bet + "€" +"\n" + ANSI_RESET);
            money -= bet;
        } else if (playerCardsValue > dealerCardsValue && dealerFinished) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_GREEN + "\nPlayer was closer to 21! He wins: " + bet * 2 + "€\n" + ANSI_RESET);
            money += bet;
        } else if (dealerCardsValue == playerCardsValue && dealerFinished) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_RED + "\nIts a draw! No money lost!\n" + ANSI_RESET);
        }
    }

    public void checkDealerJackpot() {
        if (dealerCardsValue == 21) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_RED + "\nDealer has hit a jackpot! He wins!\n" + ANSI_RESET);
            money -= bet;
        } else if (dealerCardsValue > 21) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_GREEN + "\nDealer busted! Player won : " + bet*2 + "€" + "\n" + ANSI_RESET);
            money += bet;
        } else if (dealerCardsValue > playerCardsValue && dealerFinished) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_RED + "\nDealer was closer to 21! He wins!\n" + ANSI_RESET);
            money -= bet;
        } else if (dealerCardsValue == playerCardsValue && dealerFinished) {
            gameEnd = true;
            out.print("\nPlayer: " + playerCardsValue + "\n");
            out.print("\nDealer: " + dealerCardsValue + "\n");
            out.print(ANSI_RED + "\nIts a draw! No money lost!\n" + ANSI_RESET);
        }
    }

    public void resetCards() {
        playerCardsValue = 0;
        playerCards.clear();
        dealerCardsValue = 0;
        dealerCards.clear();
    }

    public void startGame() {
        resetCards();
        gameEnd = false;
        out.print("The rules of Blackjack are as follow: \n");
        out.print("1 - Ace has the value of 1 to simplify things \n");
        out.print("2 - At the start of the game you are given 2 cards, you have the option to stand to stay with your cards, our hit to get more cards \n");
        out.print("3 - If your 2 initial cards have the value of 21, its an instant Blackjack, in other words, you instantly win your bet back multiplied by 2\n");
        out.print("4 - If you hit for more cards, and the total value of the cards is over 21, you bust and lose instantly\n");
        out.print("5 - After you stand, your turn is over, and its the dealer's turn. He will get more cards until the total of his cards are equal or above 17, if he doesn't bust while drawing, he compares both total values and sees who is closest to 21. The person closer to 21 wins\n\n");
        IntegerInputScanner minAndMaxValues = new IntegerRangeInputScanner(2, money);
        out.print("Min : 2 - Max : ∞");
        minAndMaxValues.setMessage("\nHow much do you want to bet?\n");
        minAndMaxValues.setError("\nYou cant bet that\n");
        bet = prompt.getUserInput(minAndMaxValues);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
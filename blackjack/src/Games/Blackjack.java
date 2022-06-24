package Games;

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
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //Variables
    private Socket clientSocket;
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private Dealer dealer = new Dealer();

    //Options for blackjack game
    private String[] gameOptions = {"Hit" , "Stand"};

    //More variables
    private String message;
    private boolean gameEnd;
    private int gameAnswerIndex;
    private int money = 20;
    private int bet = 0;
    private int dealerCardsValue = 0;
    private int playerCardsValue = 0;
    private ArrayList<String> dealerCards = new ArrayList<>();
    private ArrayList<String> playerCards = new ArrayList<>();
    private String allPlayerCards = "";
    private String allDealerCards = "";

    //Blackjack constructor receivees the socket and prompt, create new input and output streams;
    public Blackjack(Socket clientSocket, Prompt prompt) {
        this.clientSocket = clientSocket;
        this.prompt = prompt;
        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error creating in/out streams");
        }
    }

    public void run() {

        game();
    }

    public void game() {
        startGame();
        giveDealerCards(1);
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
                    giveDealerCards(1);
                    checkPlayerJackpot();
                    checkDealerJackpot();
                    out.print(showDealerCards());
                    out.print(showPlayerCards());
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

    public void giveDealerCards (int numberOfTimes) {
        if (dealerCardsValue >= 17) {
            out.print("Dealer is standing");
        } else {
            for (int i = 0; i < numberOfTimes; i++) {
                dealerCardsValue += dealer.drawCards();
                dealerCards.add(dealer.getCardValue() + dealer.getSuits());
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
            out.print(ANSI_GREEN  + "\n" + message + " has hit a jackpot! He wins: " + bet + "€\n" + ANSI_RESET);
            money += bet;
        } else if (playerCardsValue > 21) {
            gameEnd = true;
            out.print(ANSI_RED + "\n" + message + " busted! He lost: " + bet + "\n" + ANSI_RESET);
            money -= bet;
        }
    }

    public void checkDealerJackpot() {
        if (dealerCardsValue == 21) {
            gameEnd = true;
            out.print(ANSI_GREEN + "\nDealer has hit a jackpot! He wins!\n" + ANSI_RESET);
            money -= bet;
        } else if (dealerCardsValue > 21) {
            gameEnd = true;
            out.print(ANSI_GREEN + "\nDealer busted! Player won : " + bet + "\n" + ANSI_RESET);
            money += bet;
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
        IntegerInputScanner minAndMaxValues = new IntegerRangeInputScanner(2, money);
        out.print("Min : 2 - Max : 1000");
        minAndMaxValues.setMessage("\nHow much do you want to bet?\n");
        minAndMaxValues.setError("\nYou dont have that money you dumb\n");
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
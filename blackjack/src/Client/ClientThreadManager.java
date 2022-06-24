package Client;

import Dealer.Dealer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ClientThreadManager implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private Socket clientSocket;
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private String message;
    private String[] options = {"Play Blackjack", "Deposit more money", "Check available balance", "Quit game D:"};
    private String[] gameOptions = {"Hit" , "Stand"};
    private boolean gameEnd;

    private Dealer dealer = new Dealer();

    int answerIndex;
    int gameAnswerIndex;
    int money = 20;
    int bet = 0;
    int dealerCardsValue = 0;
    int playerCardsValue = 0;
    ArrayList<String> dealerCards = new ArrayList<>();
    ArrayList<String> playerCards = new ArrayList<>();

    String allPlayerCards = "";
    String allDealerCards = "";

    public ClientThreadManager(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
            prompt = new Prompt(in, out);
        } catch (IOException e) {
            System.out.println("Error creating output/input stream for client!");
        }
    }

    @Override
    public void run() {

        out.print("\n" +
                "░█████╗░██╗░░░██╗███╗░░██╗███╗░░██╗██╗██╗░░░░░██╗███╗░░██╗██╗░░░██╗██╗░░██╗\n" +
                "██╔══██╗██║░░░██║████╗░██║████╗░██║██║██║░░░░░██║████╗░██║██║░░░██║╚██╗██╔╝\n" +
                "██║░░╚═╝██║░░░██║██╔██╗██║██╔██╗██║██║██║░░░░░██║██╔██╗██║██║░░░██║░╚███╔╝░\n" +
                "██║░░██╗██║░░░██║██║╚████║██║╚████║██║██║░░░░░██║██║╚████║██║░░░██║░██╔██╗░\n" +
                "╚█████╔╝╚██████╔╝██║░╚███║██║░╚███║██║███████╗██║██║░╚███║╚██████╔╝██╔╝╚██╗\n" +
                "░╚════╝░░╚═════╝░╚═╝░░╚══╝╚═╝░░╚══╝╚═╝╚══════╝╚═╝╚═╝░░╚══╝░╚═════╝░╚═╝░░╚═╝\n" +
                "\n" +
                "░█████╗░░█████╗░░██████╗██╗███╗░░██╗░█████╗░\n" +
                "██╔══██╗██╔══██╗██╔════╝██║████╗░██║██╔══██╗\n" +
                "██║░░╚═╝███████║╚█████╗░██║██╔██╗██║██║░░██║\n" +
                "██║░░██╗██╔══██║░╚═══██╗██║██║╚████║██║░░██║\n" +
                "╚█████╔╝██║░░██║██████╔╝██║██║░╚███║╚█████╔╝\n" +
                "░╚════╝░╚═╝░░╚═╝╚═════╝░╚═╝╚═╝░░╚══╝░╚════╝░");

        out.print(ANSI_RED + "\n\n*************WELCOME TO AC CASINO!***************\n\n" + ANSI_RESET);

        StringInputScanner question1 = new StringInputScanner();

        question1.setMessage("Whats is your name?\n\n");
        message = prompt.getUserInput(question1);

        MenuInputScanner scanner = new MenuInputScanner(options);
        out.print("\nWelcome " + message + "! \n\n");

        try {
            while (true) {

                answerIndex = prompt.getUserInput(scanner);
                switch (answerIndex) {

                    case 1: {
                        out.print("\nGame is starting!\n");
                        game();
                        break;
                    }
                    case 2: {
                        if (money == 0) {
                            out.print("\nDeposition 10€ to help you pay the depth\n");
                        } else {
                            out.print("\nYou have more then enough money to play!\n");
                        }
                        break;
                    }
                    case 3: {
                        out.print("\nYou have " + money + "€\n");
                        break;
                    }
                    case 4: {
                        out.print("\nWhy are you leaving D: \n\n");
                        close();
                        break;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Player disconnected " + clientSocket.getInetAddress() + ":" + clientSocket.getLocalPort());
        }
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

    public void close() {
        try {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing the connection with player");
        }
    }


    public Socket getClientSocket() {
        return clientSocket;
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
}

package Client;

import Dealer.Dealer;
import Games.Blackjack;
import Server.Lobby;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.DoubleStream;

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
    private Blackjack blackjack;
    String allPlayerCards = "";
    String allDealerCards = "";

    Lobby lobby;

    public ClientThreadManager(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
            prompt = new Prompt(in, out);
            blackjack = new Blackjack(clientSocket,prompt);
        } catch (IOException e) {
            System.out.println("Error creating output/input stream for client!");
        }
    }

    /* public boolean size() {

        for (int i = 0; i <= lobby.clientsNumber; i++) {

            if (lobby.clientsNumber == 2) {

                System.out.println("2 Players connected..");
                startGame(); // ver depois.

            } else if (lobby.clientsNumber == 31) {

                System.out.println("The limit of players is 30! Please try again later.");
                gameEnd = true;

            }

        }
        return size();
    }*/


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
        blackjack.setMessage(message);
        MenuInputScanner scanner = new MenuInputScanner(options);
        out.print("\nWelcome " + message + "! \n\n");

        try {
            while (true) {
                money = blackjack.getMoney();
                answerIndex = prompt.getUserInput(scanner);
                switch (answerIndex) {

                    case 1: {
                        out.print("\nGame is starting!\n");
                        blackjack.run();
                        break;
                    }
                    case 2: {
                        if (money == 0) {
                            out.print("\nDeposition 10€ to help you pay the depth\n");
                            blackjack.setMoney(10);
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
}

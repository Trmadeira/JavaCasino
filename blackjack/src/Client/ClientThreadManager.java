package Client;

import Dealer.Dealer;
import Games.Blackjack;
import Games.Roulette;
import Server.Lobby;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

    private BufferedReader readFile;
    private FileOutputStream writeFile;

    private Socket clientSocket;
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private String message;
    private final String[] options = {"Play Blackjack","Play Roulette","Show high scores","Deposit more money", "Check available balance", "Quit game D:"};

    private Dealer dealer = new Dealer();

    int answerIndex;
    int money = 20;
    private Blackjack blackjack;
    private Roulette roulette;


    Lobby lobby;

    public ClientThreadManager(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
            prompt = new Prompt(in, out);
            blackjack = new Blackjack(clientSocket,prompt);
            roulette = new Roulette(clientSocket, prompt);
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
        try {
            readFile = new BufferedReader(new FileReader("scores.txt"));
            writeFile = new FileOutputStream("scores.txt",true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }




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
                        roulette.gameRoulette();
                    }
                    case 3: {
                        outputScores();
                        break;
                    }
                    case 4: {
                        if (money == 0) {
                            out.print("\nDeposition 10€ to help you pay the depth\n");
                            blackjack.setMoney(10);
                        } else {
                            out.print("\nYou have more then enough money to play!\n");
                        }
                        break;
                    }
                    case 5: {
                        out.print("\nYou have " + money + "€\n");
                        break;
                    }
                    case 6: {
                        out.print("\nWhy are you leaving D: \n\n");
                        createFile();
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
            readFile.close();
            writeFile.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing the connection with player");
        }
    }


    public Socket getClientSocket() {
        return clientSocket;
    }

    public void createFile() {
        String sign = "€";
        String scores = "";
        try {
            readFile = new BufferedReader(new FileReader("scores.txt"));
            writeFile = new FileOutputStream("scores.txt",true);

            while ((scores = readFile.readLine()) != null) {
                System.out.println(scores);
            if (scores.contains(message + ":" + money + "€")) {
                System.out.println("hello");
                return;
            }
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            writeFile.write(message.getBytes(StandardCharsets.UTF_8));
            writeFile.write(":".getBytes(StandardCharsets.UTF_8));
            writeFile.write(String.valueOf(money).getBytes(StandardCharsets.UTF_8));
            writeFile.write(sign.getBytes(StandardCharsets.UTF_8));
            writeFile.write("\n".getBytes(StandardCharsets.UTF_8));

        } catch (FileNotFoundException e) {
            System.out.println("Could not create file!");
        } catch (IOException e) {
            System.out.println("Could not write to file!");
        }
    }

    public void outputScores() {
        try {
            readFile = new BufferedReader(new FileReader("scores.txt"));
            String st = "";
            while ((st = readFile.readLine()) != null) {
                out.print("\n" + st + "\n");
            }

        } catch (IOException e) {
            System.out.println("Could not read file");
        }
    }
}

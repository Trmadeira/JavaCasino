package Client;

import Dealer.Dealer;
import Games.Blackjack;
import Games.Roulette;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ClientThreadManager implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private BufferedReader readFile;
    private FileOutputStream writeFile;
    private Socket clientSocket;
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private String message;
    private final String[] options = {"Play Blackjack", "Play Roulette", "Show high scores", "Deposit more money", "Check available balance", "Quit game D:"};
    int answerIndex;
    int money = 20;
    private Blackjack blackjack;
    private Roulette roulette;


    public ClientThreadManager(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());
            prompt = new Prompt(in, out);
            blackjack = new Blackjack(clientSocket, prompt);
            roulette = new Roulette(clientSocket, prompt);
        } catch (IOException e) {
            System.out.println("Error creating output/input stream for client!");
        }
    }

    @Override
    public void run() {
        try {
            readFile = new BufferedReader(new FileReader("scores.txt"));
            writeFile = new FileOutputStream("scores.txt", true);
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
                        if (money <= 2) {
                            out.print("You dont have enough money go deposit some!\n");
                            break;
                        }
                        out.print("\nGame is starting!\n");
                        blackjack.run();
                        break;
                    }
                    case 2: {
                        roulette.gameRoulette(money);
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
        ArrayList<String> plzHelp = new ArrayList<>();
        String player = message + ":" + money + "€";
        int arrayIndex = 0;

        boolean same = false;

        try {
            readFile = new BufferedReader(new FileReader("scores.txt"));

            while ((scores = readFile.readLine()) != null) {
                plzHelp.add(scores);
            }

            for (int i = 0; i < plzHelp.size(); i++) {
                String a = plzHelp.get(i);
                if (a.equals(player) || a.contains(message)) {
                    System.out.println("same");
                    arrayIndex = i;
                    same = true;
                }
            }

            if (!same) {
                plzHelp.add(player);
            } else {
                plzHelp.remove(arrayIndex);
                plzHelp.add(player);
            }

            writeFile = new FileOutputStream("scores.txt");
            writeFile.write("".getBytes(StandardCharsets.UTF_8));
            writeFile = new FileOutputStream("scores.txt", true);
            for (String a : plzHelp) {
                writeFile.write(a.getBytes(StandardCharsets.UTF_8));
                writeFile.write("\n".getBytes(StandardCharsets.UTF_8));
            }


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

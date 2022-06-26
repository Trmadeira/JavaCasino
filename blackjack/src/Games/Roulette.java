package Games;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Roulette {

    private Socket clientSocket;
    private PrintStream out;
    private InputStream in;
    private Prompt prompt;
    private Blackjack blackjack;

    int money;

    public Roulette(Socket clientSocket, Prompt prompt) {
        this.clientSocket = clientSocket;
        this.prompt = prompt;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new PrintStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            out.println("Error creating in/out streams");
        }
    }


        public void gameRoulette (int playerMoney) {
            Scanner input = new Scanner(in);
            out.println("\nWelcome to Roulette");
            out.println("\nHave you played before? Y/N");

            String check = input.nextLine();

            String gameVisual ="                _________________________________________________________________\n" +
                    "                |                       CUNNILINUX ROULETTE                     |\n" +
                    "                ----------------------------------------------------------------\n" +
                    "                | Bet                    | Pays  | Probability Win | House Edge |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Red                    | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Black                  | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Odd                    | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Even                   | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | 1 to 18                | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | 19 to 36               | 1x    | 47.37%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | 1 to 12                | 2x    | 31.58%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | 13 to 24               | 2x    | 31.58%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | 25 to 36               | 2x    | 31.58%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Six line (6 numbers)   | 5x    | 15.79%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | First five (5 numbers) | 6x    | 13.16           | 7.89%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Corner (4 numbers)     | 8x    | 10.53%          | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Street (3 numbers)     | 11x   | 7.895           | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Split (2 numbers)      | 17x   | 5.26%           | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+\n" +
                    "                | Any one number         | 35x   | 2.62%           | 5.26%      |\n" +
                    "                +------------------------+------+-----------------+-------------+";


            while (!(check.equals("y") || !(check.equals("n")))) {

                out.println("\nIncorrect, please type Y/N.");
                check = input.nextLine();

            }

            if (check.equals("Y")) {

                out.println("\nGood luck!");
                out.println("\nRemember, if you need help please type help!");

            }

            if (check.equals("N")) {
                out.println("\nYour goal is to get as much money as possible.");
                out.println("\nIf your money goes to 0€ or below, you will lose.");
                out.println("\nWARNING: Cashing out will reset the game.");
                out.println(gameVisual);
                out.println("\nYou can scroll the instructions.");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();

                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            String cashoutConfirm = "";
            String bet = "";
            String answer = "";
            this.money = playerMoney;
            int winStreak = 0;
            int gamble = 0;
            int payout = 0;
            int randomNum = 0;
            int rounds = 1;

            Random random = new Random();

            String whatDo = "\nWhat would you like to do?";
            String betOption ="\nCommands: |   red     | black  |  odd   |  even |  any  | 1to18  | 19to36 |\n" +
                                        "| sixline | firstfive | corner | street | split | 1to12 | 13to24 | 25to36 |";
            String commands = "\nCommands: | bet | money | cashout | help | restart |";

            OutOfCash:
            while (money > 0) {

                payout = 0;
                out.println("\nRound " + rounds + ".");

                out.println("\nYou have " + money + "€" + ".");
                out.println(whatDo);
                out.println(commands);
                answer = input.next();
                while (!(answer.equals("bet")) && !(answer.equals("money")) && !(answer.equals("cashout")) &&
                        !(answer.equals("restart")) && !(answer.equals("help"))) {
                    out.println("\nInvalid choice!");
                    out.println(whatDo);
                    out.println(commands);
                    answer = input.next();
                }

                while (answer.equals("money")) {
                    out.println("\nYour balance is " + money + "€" + ".");
                    out.println(whatDo);
                    out.println(commands);
                    answer = input.next();
                }

                while (answer.equals("cashout")) {
                    out.println("\nAre you sure you want to cashout " + money + "€" +"? y/n.");
                    cashoutConfirm = input.next();
                    while (!(cashoutConfirm.equals("y")) && !(cashoutConfirm.equals("n"))) {
                        out.println("\nPlease input either 'y' (yes) or 'n' (no). ");
                        out.println("\nAre you sure you want to cashout " + money + "€" + "? y/n.");
                        cashoutConfirm = input.next();
                    }
                    if (cashoutConfirm.equals("y")) {
                        out.println("\nYou have cashed out " + money + "€" + " with a " +
                                winStreak + " win streak within " + rounds + " rounds.");
                        break OutOfCash;
                    }
                    if (cashoutConfirm.equals("n")) {
                        out.println(whatDo);
                        out.println(commands);
                        answer = input.next();
                    }
                }

                while (answer.equals("restart")) {
                    out.println("\nAre you sure? Do you really want to RESTART?");
                    out.println("\nYour progress will be DELETED. Y/N.");
                    cashoutConfirm = input.next();
                    if (!(cashoutConfirm.equals("y")) && !(cashoutConfirm.equals("n"))) {
                        out.println("\nPlease pick either Y/N.");
                        out.println("\nAre you sure you want to RESTART?");
                        cashoutConfirm = input.next();
                    }
                    if (cashoutConfirm.equals("y")) {
                        out.println("\nRestarting...");
                        money = 100;
                        winStreak = 0;
                        rounds = 1;
                        out.println(whatDo);
                        out.println(commands);
                        answer = input.next();
                    }
                    if (cashoutConfirm.equals("n")) {
                        out.println(whatDo);
                        out.println(commands);
                        answer = input.next();
                    }
                }
                while (answer.equals("help")) {
                    out.println("\nYour goal is to cash out with as much money as possible.");
                    out.println("\nIf your money goes to 0€ or below, you will lose.");
                    out.println("\nWARNING: Cashing out will reset the game.");
                    out.println(gameVisual);
                    out.println("\nYou can scroll the instructions.");
                    out.println(whatDo);

                    try {

                        Thread.sleep(2500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();

                    }

                    out.println(commands);
                    answer = input.next();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }


                out.println(gameVisual);
                out.println(betOption);
                out.println("\nWhat would you like to bet on?");

                bet = input.next();
                while (!(bet.equals("red")) && !(bet.equals("black")) && !(bet.equals("even")) &&
                        !(bet.equals("odd")) && !(bet.equals("1to18")) && !(bet.equals("19to36")) &&
                        !(bet.equals("1to12")) && !(bet.equals("13to24")) && !(bet.equals("25to36")) &&
                        !(bet.equals("sixline")) && !(bet.equals("firstfive")) && !(bet.equals("corner")) &&
                        !(bet.equals("street")) && !(bet.equals("split")) && !(bet.equals("any"))) {
                    out.println("\nInvalid choice, check the table to view what you can bet on.");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("");
                    out.println(betOption);
                    out.println("");
                    out.println("\nWhat would you like to bet on?");
                    bet = input.nextLine();
                }


                String moneyBet = "\nHow much money are you going to bet?";

                out.println(moneyBet);
                gamble = input.nextInt();
                while (gamble > money) {
                    out.println("\nNice try, you're betting more than you have...");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println(moneyBet);
                    gamble = input.nextInt();
                }
                if (bet.equals("red") || bet.equals("black") || bet.equals("even") || bet.equals("odd") || bet.equals("1to18") || bet.equals("19to36")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble;
                    if (randomNum < 4738) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 47.37%!");
                    }
                    if (randomNum > 4738) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 47.37%!");
                    }
                } else if (bet.equals("1to12") || bet.equals("13to24") || bet.equals("25to36")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 2;
                    if (randomNum < 3158) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 31.58%!");
                    }
                    if (randomNum > 3158) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 31.58%!");
                    }
                } else if (bet.equals("sixline")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 5;
                    if (randomNum < 1579) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 15.79%!");
                    }
                    if (randomNum > 1579) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 15.79%!");
                    }
                } else if (bet.equals("firstfive")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 6;
                    if (randomNum < 1316) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 13.16%!");
                    }
                    if (randomNum > 1316) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 13.16%!");
                    }
                } else if (bet.equals("corner")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 8;
                    if (randomNum < 1316) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 10.53%!");
                    }
                    if (randomNum > 1316) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 10.53%!");
                    }

                } else if (bet.equals("street")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 11;

                    if (randomNum < 789) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 7.895%!");
                    }
                    if (randomNum > 789) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 7.895%!");
                    }
                } else if (bet.equals("split")) {
                    randomNum = random.nextInt(10000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("\nSpinning...");
                    payout += gamble * 17;
                    if (randomNum < 526) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 5.26%!");
                    }
                    if (randomNum > 526) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 5.26%!");
                    }
                } else if (bet.equals("any")) {
                    randomNum = random.nextInt(15000) + 1;
                    out.println("\nBetting " + gamble + "€ on " + bet + "...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    out.println("Spinning...");
                    payout += gamble * 35;
                    if (randomNum < 262) {
                        money += payout;
                        winStreak += 1;
                        out.println("\nYou won " + payout + "€ with 2.62%!");
                    }
                    if (randomNum > 262) {
                        money -= gamble;
                        winStreak = 0;
                        out.println("\nYou lost " + gamble + "€ with 2.62%!");
                    }
                }

                if (money == 0) {
                    out.println("\nYou are now broken! Go get some more money.");
                    break;
                }

                out.println("\nYou are on a " + winStreak + " win streak.");
                if (winStreak == 3) {
                    out.println("\nYou have won 500€ for your third win streak!");
                    out.println("\n500€ has been deposited into your account.");
                    money += 500;
                }
                rounds += 1;

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    public int getMoney() {
        return money;
    }
}
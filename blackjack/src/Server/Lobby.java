package Server;

import Client.ClientThreadManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

public class Lobby implements Serializable {

  /* private static final int port = 8080;
   public static final int maxUser = 30;
   private static final Object MIN_USERS = 2;

   public String gameName = "Lobby Server";
   public int clientsNumber;
   public int playersReady = 0;
   public boolean allReady = false;
   public boolean gameRunning = false;

   private final Set<ServerThread> clientConnectionThread = new LinkedHashSet<>();
   ClientThreadManager clientThreadManager;


    public static void main(String[] args) throws IOException, ClassNotFoundException {


        Lobby lobby = new Lobby();
        lobby.createLobby(lobby);



            while (true) {

                try {

                    if (areAllReady() && this.clientConnectionThreads.size() >= MIN_USERS) {

                        startGame(); // Precisa mudar isto! Para quando começar o jogo assim que tiver os min user.
                        return;

                    }

                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    private void createLobby(Lobby lobby) throws IOException, ClassNotFoundException {

        new Thread(this::waitReady).start();

        registerServer(lobby);
        runServer(lobby);

    }

    private void waitReady() { //needs testing

        while (this.clientsNumber != 2) {

            System.out.println("Wait until have 2 players.");
        }

        System.out.println("Finally there is 2 players to play.");

    }

    private void runServer(Lobby lobby) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server waiting for connections...");
            long ID = 0;

            while (!lobby.gameRunning) {

                Socket socket = serverSocket.accept();

                if (ClientThreadManager.size() >= maxUser) {

                } else {

                    ID++;
                    this.clientConnectionThread.add(new ServerThread(ID, socket));
                    System.out.println("User with the " + ID + " is now connected");

                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void registerServer(Lobby lobby) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(lobby.gameName, 8080);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject(this);

        System.out.println((String) objectInputStream.readObject());

    }

    private static boolean areAllReady() {

        return ClientThreadManager.stream().allMatch(ServerThread::isReady); // falta method.

    } */
}
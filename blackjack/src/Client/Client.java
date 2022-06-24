package Client;

import Server.Lobby;
import com.sun.tools.javac.Main;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Serializable {

    private int playerName;
    public String name = "User";
    private transient Socket socket;
    public transient Scanner input = new Scanner(System.in);
    Lobby lobby;


    public static void main(String[] args) {

        Client client = new Client();

        if (args.length > 0) {

            client.name = args[0];
        }

        try {

            client.joinServer();

        } catch (ClassNotFoundException | IOException e) {

            System.out.println("Failed to join server.");
            e.printStackTrace();
        }
    }

    public void joinServer() throws IOException, ClassNotFoundException {

        socket = new Socket(lobby.gameName, 8080);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        while(true) {

            objectOutputStream.writeObject(this);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(objectInputStream.readObject());
            System.out.println(objectInputStream.readObject());
            System.out.println(objectInputStream.readObject());

            int ready = input.nextInt();
            objectOutputStream.writeObject(ready);

            System.out.println(objectInputStream.readObject());

            objectOutputStream.writeObject(name + ": " + inputReader.readLine());

        }
    }
}

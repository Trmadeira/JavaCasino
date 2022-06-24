package Server;

import Client.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final long id;
    boolean ready = false;

    public ServerThread(long id, Socket socket) throws IOException {


        this.socket = socket;
        this.id = id;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        this.start();
    }

    public boolean isReady() {

        return ready;

    }

    public void run() {

        try {

            Client joined = (Client) in.readObject();
            System.out.println(joined.name + " is now connected.");
            out.writeObject("You joined the server.");
            out.writeObject("You are player Number " + id);
            out.writeObject("Press '1' if you are ready");
            out.flush();

            // waits for user to return ready
            while (!ready) {
                try {
                    int input = in.readInt();
                    System.out.println("input: " + input);
                    ready = input == 1;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

            out.writeObject("Waiting for players for more players...");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

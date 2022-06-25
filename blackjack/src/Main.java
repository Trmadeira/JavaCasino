import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Client.ClientThreadManager;

public class Main {

    private ExecutorService cachedPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        Main main = new Main();
        main.initiate();
    }

    public void initiate() {

        try {
            int port = 8080;
            ServerSocket serverSocket = new ServerSocket(port);
            createClient(serverSocket);
        } catch (IOException e) {
            System.out.println("Error binding server to port!");
            System.exit(1);
        }
    }

    public void createClient(ServerSocket serverSocket) {

        while (true) {
            try {
                ClientThreadManager client = new ClientThreadManager(serverSocket.accept());
                cachedPool.execute(client);
                System.out.println("New client connection from: " + client.getClientSocket().getInetAddress() + ":" + client.getClientSocket().getLocalPort());
            } catch (IOException e) {
                System.out.println("Error accepting connection from client!");
            }
        }
    }
}
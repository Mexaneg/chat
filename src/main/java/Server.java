import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5555;
    private static LinkedList<Connection> connectionList = new LinkedList<>();


    public static LinkedList<Connection> getConnectionList() {
        return connectionList;
    }

    public static void removeConnection(Connection cn) {
        connectionList.remove(cn);
    }

    public static void main(String[] args) throws IOException {
        //ServerSocket server = new ServerSocket(PORT);
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started");
        try {
            while (true) {
                Socket clientSocket = server.accept();
                try {
                    connectionList.add(new Connection(clientSocket));
                    System.out.println("Added new client connection");
                    System.out.println(connectionList.toString());
                } catch (IOException e) {
                    clientSocket.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            System.out.println("Сервер закрыт");

            server.close();

        }

    }
}

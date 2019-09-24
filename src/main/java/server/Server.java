package server;

import logs.*;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
    private static final int PORT = 5555;
    private LinkedList<Connection> connectionList = new LinkedList<>();

    public LinkedList<Connection> getConnectionList() {
        return connectionList;
    }

    public void removeConnection(Connection cn) {
        connectionList.remove(cn);
    }
    public void start() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        Log.LOG_SERVER.info("Server started");
        try {
            while (true) {
                Socket clientSocket = server.accept();
                try {
                    Connection cn = new Connection(clientSocket,this);
                    (new Thread(cn)).start();
                    connectionList.add(cn);
                    Log.LOG_SERVER.info("Added new client connection");
                    Log.LOG_SERVER.debug("Connections List: "+connectionList.toString());
                } catch (IOException e) {
                    clientSocket.close();
                    Log.LOG_SERVER.error("Failed to add new connection: "+e.getMessage());
                }

            }
        } catch (IOException e) {
            Log.LOG_SERVER.info("Connection Waiting Cycle Broken");

        } finally {

            Log.LOG_SERVER.info("Server closed");

            server.close();

        }
    }


}

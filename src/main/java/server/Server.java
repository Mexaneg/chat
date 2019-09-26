package server;

import logs.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


public class Server implements Observer{
    private static final int PORT = 5555;
    private LinkedList<Connection> connectionList = new LinkedList<>();

    @Override
    synchronized public void updateConnectionList() {
        for (Connection cn:connectionList){
            if(!cn.isActive()){
                removeConnection(cn);
            }
        }

    }

    @Override
    synchronized public void sendMessage(String message,Connection connection) {

        for (Connection cn : connectionList) {
            if (!cn.equals(connection)) {
                cn.sendMsg(message);
            }
        }
    }

    public void removeConnection(Connection cn) {
        connectionList.remove(cn);
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        Log.LOG_SERVER.info("Server started");
        ExecutorService executor =Executors.newFixedThreadPool(8);
        try {
            while (true) {
                Socket clientSocket = server.accept();
                try {
                    Connection cn = new Connection(clientSocket,this);
                    executor.execute(cn);
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
            executor.shutdown();

        } finally {

            Log.LOG_SERVER.info("Server closed");
            executor.shutdown();
            server.close();

        }
    }



}

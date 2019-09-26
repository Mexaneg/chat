package client;

import logs.*;

import java.io.*;
import java.net.*;

public class Client {
    private int port = 5555;
    private String addr = "localhost";
    private Socket clientSocket;
    private Thread read;
    private Thread write;

    private void initializeClient (){
        try {
            clientSocket = new Socket(addr, port);
            read = new Thread(new MessageReader(this));
            read.start();
            write = new Thread(new MessageWriter(this));
            write.start();


        } catch (IOException e) {
            Log.LOG_CLIENT.error("Failed to open connection: "+e.getMessage());
            this.closeConnection();
        }
        catch (NullPointerException e){
            Log.LOG_CLIENT.error("Failed to open connection: "+e.getMessage());
        }
    }


    public Client() {
        this.initializeClient();
    }

    public Client(int port, String addr) {
        this.port = port;
        this.addr = addr;
        this.initializeClient();
    }
    public void closeConnection() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                read.interrupt();
                write.interrupt();
                Log.LOG_CLIENT.info("Connect closed");
            }

        } catch (IOException e) {
            Log.LOG_CLIENT.error("Failed to close connection: "+e.getMessage());
        }catch (NullPointerException e){
            Log.LOG_CLIENT.error("Failed to open connection: "+e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }



}

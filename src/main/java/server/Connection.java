package server;

import logs.*;

import java.io.*;
import java.net.*;

public class Connection implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private BufferedWriter output;
    private Server server;


    public Connection(Socket socket, Server server) throws IOException {
        clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.server=server;
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                message = input.readLine();
                if (message.equals("stop")) {
                    this.closeConnection();
                    break;
                }
                for (Connection cn : server.getConnectionList()) {
                    if (!cn.equals(this)) {
                          cn.sendMsg(message);
                    }
                }

            }
        } catch (IOException e) {
            this.closeConnection();
        }
    }

    private void sendMsg(String message) {
        {
            try {
                output.write(message + "\n");
                output.flush();
            } catch (IOException e) {
                Log.LOG_CONNECTION.error("Message send with error: "+e.getMessage());
            }

        }


    }

    public void closeConnection() {
        try {
            if (!clientSocket.isClosed()) {

                clientSocket.close();
                input.close();
                output.close();
                for(Connection connection : server.getConnectionList()){
                    if(connection.equals(this)){
                        server.removeConnection(this);
                        Log.LOG_CONNECTION.info("Connection closed");
                    }
                }

            }
            }catch(IOException e){
            Log.LOG_CONNECTION.error("Connection closed with error: "+e.getMessage());

        }
    }

}

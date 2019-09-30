package client;

import logs.*;

import java.io.*;

public class MessageReader implements Runnable {
    private BufferedReader in;
    private Client client;


    @Override
    public void run() {
    String message;
        try {
            while (true) {
                message = in.readLine();
                if (message.equals("stop")) {
                    in.close();
                    client.closeConnection();
                    break;
                } else {
                    System.out.println("Получено сообщение:");
                    System.out.println(message);
                    Log.LOG_MESSAGE_READER.debug("Message received");
                    System.out.println("Введите сообщение");

                }
            }

        } catch (IOException e) {
            Log.LOG_MESSAGE_READER.error("Message red with error: "+e.getMessage());
            client.closeConnection();
        }

    }

    public MessageReader(Client client) {
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
        } catch (IOException e) {
            Log.LOG_MESSAGE_READER.error("Reader didn't create: "+e.getMessage());
        }


    }
}

package client;

import logs.*;

import java.io.*;

public class MessageWriter implements Runnable {

    private BufferedReader reader;
    private BufferedWriter out;
    private Client client;


    @Override
    public void run() {
        String message;
        while (true) {
            try {
                System.out.println("Введите сообщение");
                message = reader.readLine();
                if (message.equals("stop")) {
                    out.write("stop" + "\n");
                    reader.close();
                    out.flush();
                    out.close();
                    client.closeConnection();
                    break;
                } else {
                    out.write(message + "\n");
                    out.flush();
                }
                System.out.println("Сообщение отправлено");
                Log.LOG_MESSAGE_WRITER.debug("Message from " + this.toString() + " sended");
            } catch (IOException e) {
                Log.LOG_MESSAGE_WRITER.error("Message write with error: " + e.getMessage());
                client.closeConnection();
            }
        }
    }

    public MessageWriter(Client client) {
        this.client = client;
        try {
            out = new BufferedWriter(new OutputStreamWriter(client.getClientSocket().getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            Log.LOG_MESSAGE_WRITER.error("Writer didn't create: " + e.getMessage());
        }
    }
}

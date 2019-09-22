import java.io.*;
import java.net.*;

public class Connection extends Thread {
    private Socket clientSocket;
    private BufferedReader input;
    private BufferedWriter output;


    public Connection(Socket socket) throws IOException {
        clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        start();
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
                for (Connection cn : Server.getConnectionList()) {
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
                e.printStackTrace();
            }

        }


    }

    public void closeConnection() {
        try {
            if (!clientSocket.isClosed()) {

                clientSocket.close();
                input.close();
                output.close();
                for(Connection connection : Server.getConnectionList()){
                    if(connection.equals(this)){
                        connection.interrupt();
                        Server.removeConnection(this);
                    }
                }

            }
            }catch(IOException e){
            e.printStackTrace();
        }
    }

}

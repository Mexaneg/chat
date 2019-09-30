package server;

public interface Observer {
    void updateConnectionList();
    void sendMessage(String message, Connection connection);
}

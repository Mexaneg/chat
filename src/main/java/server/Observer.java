package server;

public interface Observer {
    void updateConnectionList();
    void sendCommand(Command command, Connection connection);
}

package server;

public abstract class Subject {
    private Observer observer;

    public Subject(Observer observer) {
        this.observer = observer;
    }
    public void setObserver(Observer observer){
        this.observer=observer;
    }

    public Observer getObserver() {
        return observer;
    }

    public abstract void notifyObserverDeleteConnection();
    public abstract void notifyObserverSendCommand(Command command);

}

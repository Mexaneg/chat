package server;

public class Command {
    private String message;
    private boolean isLast = false;

    public Command() {
    }

    public Command(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isLast() {
        return this.isLast;
    }
}

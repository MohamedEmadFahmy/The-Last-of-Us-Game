package exceptions;

public class InvalidTargetException extends GameActionException {
    public InvalidTargetException() {
        super();
    }
    public InvalidTargetException(String s) {
        super();
        System.out.println("Custom Message");
    }
}
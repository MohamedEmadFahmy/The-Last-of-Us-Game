package exceptions;

public class MovementException extends GameActionException{
    public MovementException() {
        super();
    }
    public MovementException(String s) {
        super();
        System.out.println("Custom Message");
    }
}

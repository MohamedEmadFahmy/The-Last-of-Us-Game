package exceptions;

public class NotEnoughActions extends GameActionException{
    public NotEnoughActions() {
        super();
    }
    public NotEnoughActions(String s) {
        super();
        System.out.println("Custom Message");
    }
}
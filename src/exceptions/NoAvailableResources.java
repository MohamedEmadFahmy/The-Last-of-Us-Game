package exceptions;

public class NoAvailableResources extends GameActionException{
    public NoAvailableResources() {
        super();
    }
    public NoAvailableResources(String s) {
        super();
        System.out.println("Custom Message");
    }
}

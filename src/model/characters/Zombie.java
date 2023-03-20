package model.characters;

public class Zombie extends Character {
    private static int ZOMBIES_COUNT = 0;

    public Zombie() {
        super("Zombie " + ++ZOMBIES_COUNT, 40, 10);
    }

    public static void main(String[] args) {
        Zombie first = new Zombie();
        Zombie second = new Zombie();
        System.out.println(first.getName());
        System.out.println(second.getName());
        System.out.println(ZOMBIES_COUNT);
}

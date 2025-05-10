public class Villain extends Entity {

    public Villain(String name, int health) {
        super(name, health);
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}

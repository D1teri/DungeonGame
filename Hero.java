public class Hero extends Entity {
    private int x, y;  // Player's position on the map

    public Hero(String name, int health) {
        super(name, health);
        this.x = 25;  // Starting position (center of the map)
        this.y = 25;
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public void heal(int amount) {
        health += amount;
    }

    public int getHealth() {
        return health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
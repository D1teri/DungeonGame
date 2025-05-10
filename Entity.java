/**
 * Entity.java
 * Abstract base class for all characters in the dungeon.
 */
public abstract class Entity {
    protected String name;
    protected int health;
    protected int x, y;

    public Entity(String name, int health) {
        this.name = name;
        this.health = health;
        this.x = 0;
        this.y = 0;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public void takeDamage(int amount) { health = Math.max(0, health - amount); }
    public void heal(int amount) { health += amount; }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
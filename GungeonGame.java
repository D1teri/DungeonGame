import java.util.*;

/**
 * GungeonGame.java
 * Main class to run the console-based Gungeon game
 */
public class GungeonGame {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

/**
 * Game.java
 * Manages the game loop and user interaction
 */
class Game {
    private Dungeon dungeon;
    private Hero player;
    private Scanner scanner;
    private List<Item> inventory;
    private Set<String> collectedItems;
    private boolean powerAttack;
    private int powerElixirCount;
    private static final int CAMERA_SIZE = 15;

    public Game() {
        dungeon = new Dungeon(50, 50); // Enlarged map
        player = new Hero("Arin the Brave", 100);
        scanner = new Scanner(System.in);
        inventory = new ArrayList<>();
        collectedItems = new HashSet<>();
        powerAttack = false;
        powerElixirCount = 0;
    }

    public void start() {
        System.out.println("Welcome to the Gungeon, a place of danger and glory!");
        System.out.println("Your mission is to survive, explore the dungeon, and defeat its guardians.");

        while (player.getHealth() > 0) {
            dungeon.printCameraView(player, CAMERA_SIZE);
            System.out.print("Move (w/a/s/d), Inventory (i), Use Item (u), or Rest (r): ");
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "i":
                    showInventory();
                    break;
                case "u":
                    useItem();
                    break;
                case "r":
                    rest();
                    break;
                default:
                    if (!dungeon.movePlayer(player, input)) {
                        System.out.println("You hit a wall. Try another direction.");
                    } else {
                        if (dungeon.checkForHazard()) {
                            player.takeDamage(15);
                            System.out.println("You stepped into a trap! HP: " + player.getHealth());
                        }
                        String positionKey = dungeon.getPlayerPositionKey();
                        if (dungeon.checkForItem() && !collectedItems.contains(positionKey)) {
                            Item item = new Item("Health Potion", "Restores 25 HP");
                            inventory.add(item);
                            collectedItems.add(positionKey);
                            dungeon.clearTileAtPlayerPosition();
                            System.out.println("You found a Health Potion!");
                        }
                        if (dungeon.checkForPowerUp() && !collectedItems.contains(positionKey) && powerElixirCount < 3) {
                            Item item = new Item("Power Elixir", "Boosts next attack");
                            inventory.add(item);
                            collectedItems.add(positionKey);
                            dungeon.clearTileAtPlayerPosition();
                            powerElixirCount++;
                            System.out.println("You found a Power Elixir!");
                        }
                        if (dungeon.checkForKey() && !collectedItems.contains(positionKey)) {
                            Item item = new Item("Silver Key", "Unlocks special rooms");
                            inventory.add(item);
                            collectedItems.add(positionKey);
                            dungeon.clearTileAtPlayerPosition();
                            System.out.println("You found a Silver Key!");
                        }
                        if (dungeon.checkForEnemy(player)) {
                            Villain enemy = dungeon.getFixedEnemyAtPosition();
                            System.out.println("A wild " + enemy.getName() + " appears!");
                            battle(enemy);
                        }
                    }
                    break;
            }
        }
        System.out.println("Game Over. Thanks for playing!");
    }

    private void battle(Villain enemy) {
        while (enemy.getHealth() > 0 && player.getHealth() > 0) {
            System.out.println("Choose action: Attack (a), Use Item (u): ");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("u")) {
                useItem();
                continue;
            } else if (action.equalsIgnoreCase("a")) {
                int damage = powerAttack ? 40 : 20;
                enemy.takeDamage(damage);
                System.out.println("You strike the " + enemy.getName() + " for " + damage + " damage! HP left: " + enemy.getHealth());
                powerAttack = false;
            } else {
                System.out.println("Invalid action.");
                continue;
            }
            if (enemy.getHealth() > 0) {
                player.takeDamage(10);
                System.out.println(enemy.getName() + " strikes back! Your HP: " + player.getHealth());
            }
        }
        if (player.getHealth() > 0) {
            System.out.println("Enemy defeated!");
        } else {
            System.out.println("You have fallen in battle...");
        }
    }

    private void showInventory() {
        System.out.println("Your Inventory:");
        if (inventory.isEmpty()) {
            System.out.println("- (Empty)");
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                Item item = inventory.get(i);
                System.out.println(i + 1 + ". " + item.getName() + ": " + item.getDescription());
            }
        }
    }

    private void useItem() {
        if (inventory.isEmpty()) {
            System.out.println("No items to use.");
            return;
        }
        showInventory();
        System.out.print("Enter item number to use: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < inventory.size()) {
                Item item = inventory.remove(choice);
                switch (item.getName().toLowerCase()) {
                    case "health potion":
                        player.heal(25);
                        System.out.println("You used a Health Potion. HP is now: " + player.getHealth());
                        break;
                    case "power elixir":
                        powerAttack = true;
                        System.out.println("You feel stronger. Next attack will be powerful!");
                        break;
                    case "silver key":
                        System.out.println("This key will help unlock future secrets.");
                        break;
                    default:
                        System.out.println("You used the " + item.getName() + ".");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void rest() {
        System.out.println("You rest for a while...");
        player.heal(10);
        System.out.println("Recovered 10 HP. Current HP: " + player.getHealth());
    }
}


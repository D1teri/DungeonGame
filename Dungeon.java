import java.util.*;

public class Dungeon {
    private char[][] map;
    private int width, height;
    private Random random = new Random();
    private Set<String> items = new HashSet<>();
    private Set<String> powerUps = new HashSet<>();
    private Set<String> keys = new HashSet<>();
    private Set<String> hazards = new HashSet<>();
    private Map<String, Villain> enemies = new HashMap<>();
    private Set<String> walls = new HashSet<>();
    private Hero player;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[height][width];
        generateMap();
    }
    //Function generates map with items, walls and enemies
    private void generateMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = '.';
            }
        }

        // Add walls
        for (int i = 0; i < 300; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            map[y][x] = '#';
            walls.add(posKey(x, y));
        }

        // Add hazards
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            map[y][x] = '^';
            hazards.add(posKey(x, y));
        }

        // Add Health Potions
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (map[y][x] == '.') {
                map[y][x] = '!';
                items.add(posKey(x, y));
            }
        }

        // Add Power Elixirs
        for(int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (map[y][x] == '.') {
                map[y][x] = '*';
                powerUps.add(posKey(x, y));
            }
        }

        // Add keys
        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            map[y][x] = 'K';
            keys.add(posKey(x, y));
        }

        // Add enemies
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            String key = posKey(x, y);
            if (map[y][x] == '.') {
                map[y][x] = 'E';
                enemies.put(key, new Villain("Goblin " + (i + 1), 40));
            }
        }
    }

    public boolean movePlayer(Hero player, String direction) {
        this.player = player;
        int x = player.getX();
        int y = player.getY();
        int newX = x, newY = y;

        switch (direction.toLowerCase()) {
            case "w": newY--; break;
            case "s": newY++; break;
            case "a": newX--; break;
            case "d": newX++; break;
            default: return false;
        }

        if (newX < 0 || newY < 0 || newX >= width || newY >= height) return false;
        if (walls.contains(posKey(newX, newY))) return false;

        player.setPosition(newX, newY);
        return true;
    }

    public void printCameraView(Hero player, int size) {
        int centerX = player.getX();
        int centerY = player.getY();
        int half = size / 2;
    
        for (int y = centerY - half; y <= centerY + half; y++) {
            for (int x = centerX - half; x <= centerX + half; x++) {
                if (x < 0 || y < 0 || x >= width || y >= height) {
                    System.out.print("# ");
                } else if (x == player.getX() && y == player.getY()) {
                    System.out.print("P ");
                } else {
                    System.out.print(map[y][x] + " ");
                }
            }
            System.out.println();
        }
    }
    
    public void clearTileAtPlayerPosition() {
        String key = player.getX() + "," + player.getY();;
        if (items.contains(key)) {
            items.remove(key);
            map[player.getY()][player.getX()] = '.';
        } else if (powerUps.contains(key)) {
            powerUps.remove(key);
            map[player.getY()][player.getX()] = '.';
        } else if (keys.contains(key)) {
            keys.remove(key);
            map[player.getY()][player.getX()] = '.';
        } else if (enemies.containsKey(key)) {
            enemies.remove(key);
            map[player.getY()][player.getX()] = '.';
        }
    }

    public boolean checkForItem() {
        return items.contains(getPlayerPositionKey());
    }

    public boolean checkForPowerUp() {
        return powerUps.contains(getPlayerPositionKey());
    }

    public boolean checkForKey() {
        return keys.contains(getPlayerPositionKey());
    }

    public boolean checkForHazard() {
        return hazards.contains(getPlayerPositionKey());
    }

    public boolean checkForEnemy(Hero player) {
        return enemies.containsKey(getPlayerPositionKey());
    }

    public Villain getFixedEnemyAtPosition() {
        return enemies.get(getPlayerPositionKey());
    }

    public String getPlayerPositionKey() {
        return posKey(player.getX(), player.getY());
    }

    private String posKey(int x, int y) {
        return x + "," + y;
    }
}
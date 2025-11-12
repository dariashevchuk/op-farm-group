import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Field {
    private int size;
    private Tile[][] tiles;

    // We often use synchronized blocks or CopyOnWriteArrayList for thread safety
    private List<Rabbit> rabbits = new CopyOnWriteArrayList<>();

    public Field(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isInBounds(int x, int y) {
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    public Tile getTile(int x, int y) {
        if (isInBounds(x, y)) {
            return tiles[x][y];
        }
        return null;
    }

    public synchronized void addRabbit(Rabbit rabbit) {
        rabbits.add(rabbit);
    }

    public synchronized void removeRabbit(Rabbit rabbit) {
        rabbits.remove(rabbit);
    }

    public synchronized Rabbit findClosestRabbit(int x, int y, int range) {
        for (Rabbit rabbit : rabbits) {
            int dx = rabbit.getX() - x;
            int dy = rabbit.getY() - y;
            if (Math.abs(dx) <= range && Math.abs(dy) <= range) {
                return rabbit;
            }
        }
        return null;
    }

    /**
     * SAVE the entire field (tiles + farmers + rabbits) to a file.
     * (We removed any "loadField" functionality as requested.)
     */
    public void saveField(String filename, FarmGroup farmGroup) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // 1) Write the field size
            writer.write(String.valueOf(size));
            writer.newLine();

            // 2) Write each tile's state (carrot/damage)
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Tile tile = tiles[i][j];
                    char carrotChar = tile.isHasCarrot() ? '1' : '0';
                    char damagedChar = tile.isDamaged() ? '1' : '0';
                    writer.write("" + carrotChar + damagedChar + " ");
                }
                writer.newLine();
            }

            // 3) Write farmers (including dog positions)
            List<Farmer> farmers = farmGroup.getMembers();
            writer.write(String.valueOf(farmers.size()));
            writer.newLine();

            // For each farmer: name, farmerX, farmerY, dogX, dogY
            for (Farmer farmer : farmers) {
                String line = farmer.getName() + " "
                        + farmer.getX() + " "
                        + farmer.getY() + " "
                        + farmer.getDog().getX() + " "
                        + farmer.getDog().getY();
                writer.write(line);
                writer.newLine();
            }

            // 4) Write rabbits (count, then each rabbit's x,y,turns)
            synchronized (this) {
                writer.write(String.valueOf(rabbits.size()));
                writer.newLine();
                for (Rabbit rabbit : rabbits) {
                    String line = rabbit.getX() + " "
                            + rabbit.getY() + " "
                            + rabbit.getTurnsRemaining();
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Field saved to " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save field: " + e.getMessage());
        }
    }

    /**
     * Display the grid in console, with farmers, dogs, rabbits, carrots, etc.
     */
    public void displayField(FarmGroup farmGroup) {
        char[][] display = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(display[i], '.');
        }

        // Mark carrots and damaged tiles
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile t = tiles[i][j];
                if (t.isDamaged()) {
                    display[i][j] = 'X'; // Damaged tile
                } else if (t.isHasCarrot()) {
                    display[i][j] = 'C'; // Carrot
                }
            }
        }

        // Mark rabbits
        synchronized (this) {
            for (Rabbit rabbit : rabbits) {
                if (isInBounds(rabbit.getX(), rabbit.getY())) {
                    display[rabbit.getX()][rabbit.getY()] = 'R';
                }
            }
        }

        // Mark dogs
        for (Farmer farmer : farmGroup.getMembers()) {
            Dog dog = farmer.getDog();
            if (isInBounds(dog.getX(), dog.getY())) {
                display[dog.getX()][dog.getY()] = 'D';
            }
        }

        // Mark farmers
        for (Farmer farmer : farmGroup.getMembers()) {
            if (isInBounds(farmer.getX(), farmer.getY())) {
                display[farmer.getX()][farmer.getY()] = 'F';
            }
        }

        // Print the grid
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(display[i][j] + " ");
            }
            System.out.println();
        }
    }
}
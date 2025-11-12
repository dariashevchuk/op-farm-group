
public class Farmer extends Entity {
    private String name;
    private Dog dog;

    public Farmer(String name, int x, int y, Field field) {
        super(x, y, field);
        this.name = name;
        this.dog = new Dog(name + "'s Dog", x, y, field);
    }

    public Dog getDog() {
        return dog;
    }

    // NEW: Provide a getter for the farmer's name so we can save/load it
    public String getName() {
        return name;
    }

    public void plantCarrot() {
        Tile tile = field.getTile(x, y); // Get tile at farmer's current position
        if (tile != null && !tile.isHasCarrot() && !tile.isDamaged()) {
            tile.plantCarrot(); // Plant carrot
            System.out.println(name + " planted a carrot at (" + x + ", " + y + ").");
        }
    }

    public void repairAndReplant() {
        Tile tile = field.getTile(x, y); // Get tile at farmer's current position
        if (tile != null && tile.isDamaged()) {
            tile.repair(); // Repair damaged tile
            tile.plantCarrot(); // Plant carrot after repair
            System.out.println(name + " repaired and replanted at (" + x + ", " + y + ").");
        }
    }

    @Override
    public void run() {
        Thread dogThread = new Thread(dog);
        dogThread.start(); // Start dog's thread

        while (running) {
            try {
                moveRandomly(); // Move farmer randomly

                // Decide action: 50% plant carrot, 50% repair if needed
                if (Math.random() < 0.5) {
                    plantCarrot();
                } else {
                    repairAndReplant();
                }

                Thread.sleep(500); // Simulate time passing
            } catch (InterruptedException e) {
                running = false;
            }
        }

        dog.stopEntity(); // Stop dog's thread when farmer stops
    }
}




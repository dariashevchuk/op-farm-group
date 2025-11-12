public class Rabbit extends Entity {
    private int turnsRemaining;

    public Rabbit(int x, int y, Field field, int turnsRemaining) {
        super(x, y, field);
        this.turnsRemaining = turnsRemaining;
    }

    // NEW: Getter for turnsRemaining so we can save it
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    private void eatCarrot() {
        Tile tile = field.getTile(x, y);
        if (tile != null && tile.isHasCarrot()) {
            tile.setHasCarrot(false);
            tile.damage();
            System.out.println("Rabbit ate a carrot at (" + x + ", " + y + ").");
        }
    }

    @Override
    public void run() {
        while (running && turnsRemaining > 0) {
            try {
                moveRandomly();
                eatCarrot();
                turnsRemaining--;
                Thread.sleep(400);
            } catch (InterruptedException e) {
                running = false;
            }
        }

        field.removeRabbit(this);
        System.out.println("Rabbit left the field.");
    }
}




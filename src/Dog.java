public class Dog extends Entity {
    private String name;

    public Dog(String name, int x, int y, Field field) {
        super(x, y, field);
        this.name = name;
    }

    // NEW: We can add a setter for position so we can restore
    // the dog's location from file.
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void chaseRabbit() {
        // Find the closest rabbit within range 5
        Rabbit rabbit = field.findClosestRabbit(x, y, 5);
        if (rabbit != null) {
            // Move one step closer in x and y
            int dx = Integer.compare(rabbit.getX(), x); // -1, 0, or 1
            int dy = Integer.compare(rabbit.getY(), y); // -1, 0, or 1
            x += dx;
            y += dy;
            ensureInBounds();

            // Check if the dog has reached the rabbit's position
            if (this.x == rabbit.getX() && this.y == rabbit.getY()) {
                field.removeRabbit(rabbit);
                System.out.println(name + " removed a rabbit at (" + x + ", " + y + ").");
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                // If there's a rabbit in range, chase it; otherwise move randomly
                Rabbit rabbit = field.findClosestRabbit(x, y, 5);
                if (rabbit != null) {
                    chaseRabbit();
                } else {
                    moveRandomly();
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}



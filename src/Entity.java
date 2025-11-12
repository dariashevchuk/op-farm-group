import java.util.*;

/**
 * An abstract Entity class demonstrating inheritance.
 * Farmer, Dog, and Rabbit will extend this class.
 */
public abstract class Entity implements Runnable {
    protected int x;
    protected int y;
    protected Field field;
    protected boolean running = true; // Control the thread loop

    public Entity(int x, int y, Field field) {
        this.x = x;
        this.y = y;
        this.field = field;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    // Ensures that the entity's position stays within bounds
    protected void ensureInBounds() {
        x = Math.max(0, Math.min(x, field.getSize() - 1));
        y = Math.max(0, Math.min(y, field.getSize() - 1));
    }

    // Move the entity randomly to an adjacent tile, if in bounds
    protected void moveRandomly() {
        int newX = x + (int)(Math.random() * 3) - 1;  // -1, 0, 1
        int newY = y + (int)(Math.random() * 3) - 1;  // -1, 0, 1
        if (field.isInBounds(newX, newY)) {
            x = newX;
            y = newY;
        }
        ensureInBounds(); // Clamp position to ensure within bounds
    }


    // Abstract run method from Runnable.
    // Subclasses will override to provide specific behavior.
    @Override
    public abstract void run();

    // Allows stopping the thread externally
    public void stopEntity() {
        running = false;
    }
}

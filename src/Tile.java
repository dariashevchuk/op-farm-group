public class Tile {
    private boolean hasCarrot;
    private boolean isDamaged;
    private int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasCarrot = false;
        this.isDamaged = false;
    }

    // Synchronized getters and setters
    public synchronized boolean isHasCarrot() {
        return hasCarrot;
    }

    public synchronized void setHasCarrot(boolean hasCarrot) {
        this.hasCarrot = hasCarrot;
    }

    public synchronized boolean isDamaged() {
        return isDamaged;
    }

    /**
     * Plant a carrot if the tile is not damaged.
     */
    public synchronized void plantCarrot() {
        if (!isDamaged) {
            hasCarrot = true;
        }
    }

    /**
     * Mark this tile as damaged and remove any carrot there.
     */
    public synchronized void damage() {
        this.isDamaged = true;
        this.hasCarrot = false;
    }

    /**
     * Repair this tile (no longer damaged).
     */
    public synchronized void repair() {
        this.isDamaged = false;
    }
}

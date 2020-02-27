package main;

import main.exceptions.NullDimensionException;

public class Baggage {

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private final int length;
    private final int height;
    private final int width;
    private final int weight;

    /* =======================
           CONSTRUCTORS
    ======================= */

    public Baggage(int length, int height, int width, int weight) throws NullDimensionException {
        if (length == 0 || height == 0 || width == 0){
            throw new NullDimensionException("Dimensions cannot be 0");
        } else {
            this.length = length;
            this.height = height;
            this.width = width;
            this.weight = weight;
        }
    }

    /* =======================
            ACCESSORS
    ======================= */

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getWeight() {
        return weight;
    }

    public int getVolume() { return length*width*height/1000; }
}

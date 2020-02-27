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

    /**
     * @param length
     * @param height
     * @param width
     * @param weight
     * @throws NullDimensionException
     */
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

    /**
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return
     */
    public int getVolume() { return length*width*height/1000; }
}

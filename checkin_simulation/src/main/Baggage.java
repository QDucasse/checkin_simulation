package main;

import main.exceptions.NegativeDimensionException;
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
     * Baggage class holding dimensions (height, width and length) as well as weight of a passenger's baggage.
     * @param length
     *    The length of the baggage.
     * @param height
     *    The height of the baggage.
     * @param width
     *    The width of the baggage.
     * @param weight
     *    The weight of the baggage
     * @throws NullDimensionException
     *    The attributes cannot be null.
     * @throws NegativeDimensionException
     *    The attributes cannot be negative.
     */
    public Baggage(int length, int height, int width, int weight) throws NullDimensionException, NegativeDimensionException {
        if (length == 0 || height == 0 || width == 0 || weight == 0){
            throw new NullDimensionException("Attributes cannot be 0");
        } else if (length <= 0 || height <= 0 || width <= 0 || weight <= 0) {
            throw new NegativeDimensionException("Attributes cannot be negative");
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
     * Getter of the length instance variable
     * @return length
     *    The length of the baggage
     */
    public int getLength() {
        return length;
    }

    /**
     * Getter of the height instance variable
     * @return height
     *    The height of the baggage
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter of the width instance variable
     * @return width
     *    The width of the baggage
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter of the weight instance variable
     * @return weight
     *    The weight of the baggage
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Getter of the volume instance variable
     * @return volume
     *    The volume of the baggage
     */
    public int getVolume() { return length*width*height/1000; }
}

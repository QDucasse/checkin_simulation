package main;

public class Baggage {

    private final int length;
    private final int height;
    private final int width;
    private final int weight;

    public Baggage(int length, int height, int width, int weight) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.weight = weight;
    }

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

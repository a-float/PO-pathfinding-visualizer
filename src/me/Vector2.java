package me;

import java.util.Objects;
import java.util.Random;

/**
 * Class representing a 2D Vector of integer coordinates
 */
public class Vector2 {
    static Random random = new Random();
    private int x;
    private int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public static Vector2 subtract(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x - vec2.x, vec1.y - vec2.y);
    }

    public static Vector2 createRandom(int widthRange, int heightRange) {
        return new Vector2(random.nextInt(widthRange), random.nextInt(heightRange));
    }

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public static Vector2 up() {
        return new Vector2(0, 1);
    }

    public static Vector2 right() {
        return new Vector2(1, 0);
    }

    public static Vector2 down() {
        return new Vector2(0, -1);
    }

    public static Vector2 left() {
        return new Vector2(-1, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void add(Vector2 other) {
        x += other.x;
        y += other.y;
    }

    public void subtract(Vector2 other) {
        x -= other.x;
        y -= other.y;
    }

    public Vector2 opposite() {
        return new Vector2(-x, -y);
    }

    public Vector2[] getAdjacentPositions() {
        Vector2[] result = new Vector2[4];
        int counter = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx * dy == 0 && dx + dy != 0) {
                    result[counter] = new Vector2(x + dx, y + dy);
                    counter++;
                }
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2 other = (Vector2) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }
}

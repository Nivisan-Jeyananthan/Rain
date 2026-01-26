package ch.nivisan.rain.utils;

public class Vector2 {
    private int x, y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public static Vector2 addVector(Vector2 start, Vector2 goal) {
        start.x += goal.x;
        start.y += goal.y;
        return start;
    }

    public static Vector2 subtractVector(Vector2 start, Vector2 goal) {
        start.x -= goal.x;
        start.y -= goal.y;
        return start;
    }

    public static float getDistance(Vector2 start, Vector2 goal) {
        float dx = start.x - goal.x;
        float dy = start.y - goal.y;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    public Vector2 addVector(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2 subtractVector(Vector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    public float getDistance(Vector2 goal) {
        float x = this.x - goal.x;
        float y = this.y - goal.y;
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public int getY() {
        return y;
    }

    public Vector2 setY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public Vector2 setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2 toCompare) {
            return this.x == toCompare.x && this.y == toCompare.y;
        }
        return false;
    }
}

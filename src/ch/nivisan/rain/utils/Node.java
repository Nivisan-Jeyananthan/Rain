package ch.nivisan.rain.utils;

public class Node {
    public final Vector2 tile;
    public final Node parent;
    // sum of nodes we have visited since our start
    public final float gCost;
    // straight line to goal using birds point of view
    public final float hCost;
    public final float totalCost;

    public Node(Vector2 tile, Node parent, float gCost, float hCost)
    {
        this.tile = tile;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.totalCost = + this.gCost + this.hCost;
    }
}

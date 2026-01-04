package ch.nivisan.rain.level;

public class TileCoordinate {
    private final int x;
    private final int y;
    private final int tileSize = 16;

    public TileCoordinate(int x,int y){
        this.x = x * tileSize;
        this.y = y * tileSize;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int[] asArray(){
        var result = new int[2];
        result[0] = x;
        result[1] = y;

        return result;
    }
}

package ch.nivisan.rain.entity.mob;

public enum Direction {
    None(-1),North(0),East(1),South(2),West(3);
    private final int dir;

    Direction(int dir){
        this.dir = dir;
    }

    public int getNumValue(){
        return dir;
    }
}

package Modele;

import java.util.Random;

/**
 * An enum class used by the entities to move in the grid.
 */
public enum Depl {
    UP, RIGHT, DOWN, LEFT;
    private static Random r=new Random();
    private static Depl[] vals = values();

    public Depl getSymmetrical(){
        return vals[(this.ordinal()+(r.nextInt(2)==1?1:3))% vals.length];
    }

    public static Depl getRandom(){
        return vals[r.nextInt(vals.length)];
    }
}

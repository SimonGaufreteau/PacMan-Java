package Modele;

import java.util.Random;

public enum Depl {
    HAUT,DROIT,BAS,GAUCHE;
    private static Random r=new Random();
    private static Depl[] vals = values();

    public Depl getNext(){
        return vals[(this.ordinal()+1) % vals.length];
    }

    public Depl getSymetrical(){
        return vals[(this.ordinal()+(r.nextInt(2)==1?1:3))% vals.length];
    }

    public static Depl getRandom(){
        return vals[r.nextInt(vals.length)];
    }
}

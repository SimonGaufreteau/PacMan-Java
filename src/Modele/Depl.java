package Modele;

import java.util.Random;

public enum Depl {
    HAUT,BAS,GAUCHE,DROIT,AUCUN;
    private static Depl[] vals = values();

    public Depl getNext(){
        return vals[(this.ordinal()+1) % vals.length];
    }

    public static Depl getRandom(){
        return vals[new Random().nextInt(vals.length)];
    }
}

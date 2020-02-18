package Modele;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grille {
    private ObjStatic[][] grille;
    private Map<Entite, Point> map;
    private int SIZE_X;
    private int SIZE_Y;


    public Grille(){
        map = new HashMap<>();
        int n=4;
        Random r = new Random();
        for (int i=0;i<n;i++){
            map.put(new Fantome(),new Point(r.nextInt(SIZE_X),r.nextInt(SIZE_Y)));
        }

        SimplePacMan spm = new SimplePacMan(this);
        map.put(spm,new Point(0,0));
    }

    public ObjStatic[][] getGrille() {
        return grille;
    }

    public void depl(Depl haut,Entite e) {

    }

    public boolean OkDepl(Depl haut,Entite e) {
        return true;
    }
}

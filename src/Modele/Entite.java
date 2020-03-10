package Modele;

import java.util.Observable;
import java.util.Random;


public abstract class Entite extends Observable implements Runnable {
    protected Grille grille;
    protected int delay;
    protected Depl direction;
    protected Random r=new Random();

    protected void randomAction() throws Exception {
        int delta = r.nextInt(4);
        Depl depl=Depl.values()[delta];
        if(grille.OkDepl(depl,this))
            grille.depl(depl,this);
        setChanged();
    }




    public abstract void start();
}

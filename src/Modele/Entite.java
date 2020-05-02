package Modele;

import java.util.Random;


public abstract class Entite extends Thread {
    protected Grille grille;
    protected int delay;
    protected Depl direction;
    protected Random r=new Random();
    protected boolean running=false;
    protected int lives=1;

    protected void randomAction() throws Exception {
        int delta = r.nextInt(4);
        Depl depl=Depl.values()[delta];
        if(grille.OkDepl(depl,this))
            grille.depl(depl,this);
    }

    public void stopEntite(){
        running=false;
    }

    public boolean isRunning(){
        return running;
    }



    public abstract void start();
}

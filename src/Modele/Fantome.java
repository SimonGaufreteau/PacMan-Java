package Modele;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fantome extends Entite {
    private Random r = new Random();
    private int delay;

    public Fantome(Grille grille,int delay){
        this.grille=grille;
        this.delay=delay;
    }

    @Override
    public void action() throws Exception {
        int delta = r.nextInt(4);
        Depl depl=Depl.values()[delta];
        if(grille.OkDepl(depl,this))
            grille.depl(depl,this);
        setChanged();
    }

    @Override
    public void start() {
        new Thread(this).start();
    }


    @Override
    public void run() {
        while(true){
            try {
                action();
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifyObservers(); // notification de l'observer
            try {
                Thread.sleep(delay); // pause
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

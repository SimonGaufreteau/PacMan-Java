/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fred
 */
public class SimplePacMan extends Entite {
    Random r = new Random();

    public SimplePacMan(Grille grille,int delay){
        this.grille=grille;
        this.delay=delay;
        direction=null;
    }
    @Override
    public void run() {
        running=true;
        while(running) { // spm descent dans la grille à chaque pas de temps
            try {
                action(direction);
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

    public void action(Depl direction) throws Exception {
        if(grille.OkDepl(direction,this))
            grille.depl(direction,this);
        setChanged();
    }


    @Override
    public void start() {
        new Thread(this).start();
    }

    public void setDirection(Depl depl){
        this.direction=depl;
    }
}

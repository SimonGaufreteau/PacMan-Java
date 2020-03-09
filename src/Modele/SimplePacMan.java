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
    private Grille grille;
    Random r = new Random();
    int delay;
    Depl direction;


    public SimplePacMan(Grille grille,int delay){
        this.grille=grille;
        this.delay=delay;
        direction=Depl.AUCUN;
    }
    @Override
    public void run() {
        while(true) { // spm descent dasn la grille à chaque pas de temps
            try {
                move(direction);
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

    public void move(Depl depl) throws Exception {
        if(grille.OkDepl(depl,this))
            grille.depl(depl,this);
        setChanged();
    }

    public void setDirection(Depl depl){
        this.direction=depl;
    }
}

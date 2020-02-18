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


    public SimplePacMan(Grille grille){
        this.grille=grille;
    }
    @Override
    public void run() {
        while(true) { // spm descent dasn la grille à chaque pas de temps
            action();
            notifyObservers(); // notification de l'observer

            try {
                Thread.sleep(300); // pause
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }


    @Override
    public void action() {
        int delta = r.nextInt(3);

        switch (delta) {
            case 0 :
                if(grille.OkDepl(Depl.HAUT,this))
                    grille.depl(Depl.HAUT,this);
                break;
            case 1 :

        }

        //System.out.println(x + " - " + y);

        setChanged();
    }
}

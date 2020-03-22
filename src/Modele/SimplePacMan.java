/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimplePacMan extends Entite {
    Random r = new Random();
    private Depl cachedDirection;
    private boolean invisible;
    private static int INVISIBLE_DELAY=3000;
    private static int UNTOUCHABLE_DELAY=3000;
    private boolean untouchable;
    private int timeLeftUntouchable;
    private int timeLeftInvisible;

    public SimplePacMan(Grille grille,int delay){
        this.grille=grille;
        this.delay=delay;
        direction=null;
        cachedDirection=null;
        lives=3;
        invisible=false;
        timeLeftInvisible=0;
        timeLeftUntouchable=0;
        untouchable=false;
    }
    @Override
    public void run() {
        running=true;
        while(running) { // spm descent dans la grille à chaque pas de temps
            if(timeLeftInvisible<=0 && invisible){
                invisible=false;
                System.out.println("Pacman no longer invisible !");
            }
            if (timeLeftUntouchable <= 0 && untouchable) {
                untouchable=false;
                System.out.println("Pacman no longer untouchable !");
            }
            try {
                action();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(invisible)
                    timeLeftInvisible-=delay;
                if(untouchable)
                    timeLeftUntouchable-=delay;
                Thread.sleep(delay); // pause
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void action() throws Exception {
        if(grille.OkDepl(cachedDirection,this)){
            this.direction=cachedDirection;
            this.cachedDirection=null;
            grille.depl(direction,this);
        }
        else if(grille.OkDepl(direction,this))
            grille.depl(direction,this);
        else{//Impossible to go in any direction
            this.direction=null;
        }
    }


    @Override
    public void start() {
        new Thread(this).start();
    }

    public void setDirection(Depl depl){
        this.direction=depl;
    }

    public void setCachedDirection(Depl cachedDirection) {
        this.cachedDirection = cachedDirection;
    }

	public boolean hasLives() {
        return lives>0;
	}

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible() {
        invisible=true;
        untouchable=false;
        timeLeftUntouchable=0;
        timeLeftInvisible=INVISIBLE_DELAY;
    }

    public void setUntouchable(){
        untouchable=true;
        timeLeftUntouchable=UNTOUCHABLE_DELAY;
    }

    public boolean isUntouchable() {
        return untouchable;
    }

    public int getLives(){
        return lives;
    }
}

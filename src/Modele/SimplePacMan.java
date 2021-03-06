package Modele;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread used to model the PacMan entity. Inherits of the {@link Entity} class which inherits of {@link Thread}
 * @see Entity
 * @see Ghost
 * @see ModelGrid
 */
public class SimplePacMan extends Entity {
    public static int MAX_HEALTH = 3;
    private Depl cachedDirection;
    private boolean invisible;
    private final static int INVISIBLE_DELAY=3000;
    private final static int UNTOUCHABLE_DELAY=3000;
    private final static int PACMAN_DELAY=100;
    private boolean untouchable;
    private int timeLeftUntouchable;
    private int timeLeftInvisible;

    public SimplePacMan(ModelGrid modelGrid){
        this.delay=PACMAN_DELAY;
        reset(modelGrid);
    }

    public void reset() {
        reset(modelGrid);
    }

    public void reset(ModelGrid modelGrid){
        this.modelGrid = modelGrid;
        direction=null;
        cachedDirection=null;
        lives=MAX_HEALTH;
        invisible=false;
        timeLeftInvisible=0;
        timeLeftUntouchable=0;
        untouchable=false;
    }

    @Override
    public synchronized void run() {
        running.set(true);
        while(running.get()) {
            if(timeLeftInvisible<=0 && invisible){
                invisible=false;
            }
            if (timeLeftUntouchable <= 0 && untouchable) {
                untouchable=false;
            }
            try {
                action();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //Updates the status for each delay
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

    /**
     * Moves Pacman in the cachedDirection. If the move is impossible, the direction is set to null.
     */
    public void action() throws Exception {
        if(modelGrid.OkDepl(cachedDirection,this)){
            this.direction=cachedDirection;
            this.cachedDirection=null;
            modelGrid.depl(direction,this);
        }
        else if(modelGrid.OkDepl(direction,this))
            modelGrid.depl(direction,this);
        else{//Impossible to go in any direction
            this.direction=null;
        }
    }


    @Override
    public void start() {
        new Thread(this).start();
    }


    // GETTERS AND SETTERS
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

    public Depl getDirection() {
        return direction;
    }


}

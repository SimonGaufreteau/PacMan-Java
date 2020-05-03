package Modele;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple class used to model a Ghost in a PacMan game. These entities are updated each DELAY and are stopped at the end of the game.
 * @see Entity
 * @see SimplePacMan
 * @see ModelGrid
 * @see Controller.ThreadController
 */
public class Ghost extends Entity {
    public Ghost(ModelGrid modelGrid, int delay){
        this.modelGrid = modelGrid;
        this.delay=delay;
        this.direction=null;
    }

    public void action() throws Exception {
        changeDirection();
    }

    /**
     * The main algorithm used to make the AI of the Ghosts work. This is basically a random move.
     */
    private void changeDirection() {
        if(direction==null) direction=Depl.getRandom();
        Depl symmetrical = direction.getSymmetrical();
        try {
            if (modelGrid.OkDepl(symmetrical, this)) {
                this.direction = symmetrical;
                modelGrid.depl(symmetrical, this);
            } else changeDirectionRecur(direction);
        } catch (Exception ignored){}
    }

    private synchronized void changeDirectionRecur(Depl depl) throws Exception{
        if(modelGrid.OkDepl(depl,this)){
            this.direction=depl;
            modelGrid.depl(depl,this);
        }
        else changeDirectionRecur(Depl.getRandom());

    }

    @Override
    public void start() {
        new Thread(this).start();
    }


    @Override
    public void run() {
        running=true;
        while(running){
            try {
                action();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

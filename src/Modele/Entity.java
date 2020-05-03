package Modele;

/**
 * A small definition of an entity in this project : a Thread that moves following a direction each DELAY of frames.
 * @see SimplePacMan
 * @see Ghost
 */
public abstract class Entity extends Thread {
    protected ModelGrid modelGrid;
    protected int delay;
    protected Depl direction;
    protected boolean running=false;
    protected int lives=1;

    public void stopEntite(){
        running=false;
    }

    public abstract void start();
}

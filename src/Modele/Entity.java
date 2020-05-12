package Modele;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A small definition of an entity in this project : a Thread that moves following a direction each DELAY of frames.
 * @see SimplePacMan
 * @see Ghost
 */
public abstract class Entity extends Thread {
    protected ModelGrid modelGrid;
    protected int delay;
    protected Depl direction;
    protected AtomicBoolean running=new AtomicBoolean(false);
    protected int lives=1;

    public void stopEntity(){
        running.set(false);
    }

    public abstract void start();
}

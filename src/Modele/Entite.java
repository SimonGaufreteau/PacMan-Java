package Modele;

import java.util.Observable;


public abstract class Entite extends Observable implements Runnable {
    protected Grille grille;
    public abstract void action() throws Exception;

    public abstract void start();
}

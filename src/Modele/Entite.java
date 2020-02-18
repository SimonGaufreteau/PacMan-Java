package Modele;

import java.util.Observable;


public abstract class Entite extends Observable implements Runnable {
    public abstract void action();
}

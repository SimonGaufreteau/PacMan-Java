package Modele;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fantome extends Entite {
    private Random r = new Random();

    public Fantome(Grille grille,int delay){
        this.grille=grille;
        this.delay=delay;
        this.direction=null;
    }

    public void action() throws Exception {
        changeDirection();
    }

    private void changeDirection() throws Exception {
        if(direction==null) direction=Depl.getRandom();
        Depl symetrical = direction.getSymetrical();
        if(grille.OkDepl(symetrical,this)){
            this.direction=symetrical;
            grille.depl(symetrical,this);
            setChanged();
        }
        else changeDirectionRecur(direction);
    }

    private void changeDirectionRecur(Depl depl) throws Exception{
        if(grille.OkDepl(depl,this)){
            this.direction=depl;
            grille.depl(depl,this);
            setChanged();
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
            notifyObservers(); // notification de l'observer
            try {
                Thread.sleep(delay); // pause
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

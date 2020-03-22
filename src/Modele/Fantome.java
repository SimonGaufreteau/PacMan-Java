package Modele;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Fantome extends Entite {
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
        try {
            if (grille.OkDepl(symetrical, this)) {
                this.direction = symetrical;
                grille.depl(symetrical, this);
            } else changeDirectionRecur(direction);
        } catch (Exception ignored){
            //TODO : remove
            System.out.println("Problem with changeDirection");
        }
    }

    private synchronized void changeDirectionRecur(Depl depl) throws Exception{
        if(grille.OkDepl(depl,this)){
            this.direction=depl;
            grille.depl(depl,this);
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
                Thread.sleep(delay); // pause
            } catch (InterruptedException ex) {
                Logger.getLogger(SimplePacMan.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

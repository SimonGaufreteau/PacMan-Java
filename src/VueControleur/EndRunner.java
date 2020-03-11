package VueControleur;

import Modele.Grille;

public class EndRunner implements Runnable {
	boolean running=false;
	Grille grille;
	int delay;

	public EndRunner(Grille grille,int delay){
		this.grille=grille;
		this.delay=delay;
	}

	public void start(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		running=true;
		while(running){
			endofGame();
			try {
				Thread.sleep(delay);
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}

	}

	private void endofGame() {
		if (grille.getNbBonusLeft()==0){
			running=false;

		}
	}
}

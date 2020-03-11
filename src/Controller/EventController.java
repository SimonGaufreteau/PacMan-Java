package Controller;

import Modele.Depl;
import Modele.Grille;
import Modele.SimplePacMan;

public class EventController {
	private SimplePacMan spm;
	public EventController(Grille modelGrid) {
		spm=modelGrid.getPacMan();
	}

	public void moveLeft(){
		spm.setDirection(Depl.GAUCHE);
	}

	public void moveRight(){
		spm.setDirection(Depl.DROIT);
	}

	public void moveUp(){
		spm.setDirection(Depl.HAUT);
	}

	public void moveDown(){
		spm.setDirection(Depl.BAS);
	}
}

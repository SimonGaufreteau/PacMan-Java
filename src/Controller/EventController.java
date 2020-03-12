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
		spm.setCachedDirection(Depl.GAUCHE);
	}

	public void moveRight(){
		spm.setCachedDirection(Depl.DROIT);
	}

	public void moveUp(){
		spm.setCachedDirection(Depl.HAUT);
	}

	public void moveDown(){
		spm.setCachedDirection(Depl.BAS);
	}
}

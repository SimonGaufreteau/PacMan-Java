package Controller;

import Modele.Depl;
import Modele.Grille;
import Modele.SimplePacMan;
import javafx.scene.Node;
public class EventController {
	private SimplePacMan spm;
	
	public EventController(Grille modelGrid) {
		this.spm=modelGrid.getPacMan();

	}

	public void reset(Grille modelGrid){
		spm=modelGrid.getPacMan();
	}

	public void setBindings(Node root){
		root.setOnKeyPressed(event -> {
			try {
				/*
				 * Note : this is a bit useless for now because nothing else than setDirection is done in the class
				 * but to keep the MVC pattern clean and for future updates, we keep this design (for now).
				 */
				switch (event.getCode()){
					case LEFT:
						moveLeft();
						break;
					case RIGHT:
						moveRight();
						break;
					case UP:
						moveUp();
						break;
					case DOWN:
						moveDown();
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
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

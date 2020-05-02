package Controller;

import Modele.Depl;
import Modele.Grille;
import Modele.SimplePacMan;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class EventController {
	private SimplePacMan spm;
	private Grille modelGrid;
	private ImageView[][] tab;
	private ThreadController threadController;
	private ImageView[] lifetab;

	public EventController(Grille modelGrid, ThreadController threadController, ImageView[][] tab, ImageView[] lifetab) {
		this.spm=modelGrid.getPacMan();
		this.threadController=threadController;
		this.modelGrid=modelGrid;
		this.tab=tab;
		this.lifetab = lifetab;
	}

	private void reset(){
		spm=modelGrid.getPacMan();
	}

	public void setBindings(BorderPane root){
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
					default:
						root.getCenter().requestFocus();
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


	public void handleRestart(ActionEvent actionEvent) {
			threadController.interruptThreads();
			threadController.resetGrid();
			reset();
			threadController.resetThreads(tab,modelGrid,lifetab);
			threadController.startThreads();
	}

	public void handleStop(ActionEvent actionEvent){
		threadController.stopAll();
		Platform.exit();

	}

}

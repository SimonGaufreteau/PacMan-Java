package Controller;

import Modele.Depl;
import Modele.Grille;
import Modele.SimplePacMan;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Iterator;

public class EventController {
	private SimplePacMan spm;
	private Grille modelGrid;
	private ImageView[][] tab;
	private ThreadController threadController;
	private ImageView[] lifetab;
	private ObservableList<Menu> diffs;

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

	public void setMenus(ObservableList<Menu> menus){
		this.diffs =menus;
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
		modelGrid.setFANTOME_NUMBER(getGhosts());
		modelGrid.changeDifficulty(getDifficulty());
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

	private int getDifficulty(){
		Iterator<MenuItem> it = diffs.get(1).getItems().iterator();
		int i=1;
		while (it.hasNext()){
			MenuItem diff = it.next();
			if(diff instanceof RadioMenuItem && ((RadioMenuItem) diff).isSelected()){
				return i;
			}
			i++;
		}
		return 1;
	}

	private int getGhosts(){
		ObservableList<MenuItem> it = diffs.get(2).getItems();
		Slider slider = (Slider)it.get(0).getGraphic();
		int nb =(int)slider.getValue();
		return nb;
	}

}

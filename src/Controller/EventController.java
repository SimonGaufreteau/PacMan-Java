package Controller;

import Modele.Depl;
import Modele.ModelGrid;
import Modele.SimplePacMan;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

import java.util.Iterator;

/**
 * Controls both the key events and the menu described in the main.fxml file.
 * @see Vue.MainVue
 */
public class EventController {
	private SimplePacMan spm;
	private ModelGrid modelGrid;
	private ObservableList<Menu> diffs;

	public EventController(ModelGrid modelGrid) {
		this.spm=modelGrid.getPacMan();
		this.modelGrid=modelGrid;
	}

	private void reset(){
		spm=modelGrid.getPacMan();
	}

	/**
	 * Sets the bindings to play the game. The 4 direction keys are binding to the PacMan entity.
	 * @param root The BorderPane used to display the game.
	 */
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
		spm.setCachedDirection(Depl.LEFT);
	}

	public void moveRight(){
		spm.setCachedDirection(Depl.RIGHT);
	}

	public void moveUp(){
		spm.setCachedDirection(Depl.UP);
	}

	public void moveDown(){
		spm.setCachedDirection(Depl.DOWN);
	}


	/**
	 * Restart a game with the parameter selected in the menus.
	 */
	public void handleRestart(ActionEvent actionEvent) throws Exception {
		modelGrid.stopEntities();
		modelGrid.setGHOST_NUMBER(getGhosts());
		modelGrid.changeDifficulty(getDifficulty());
		reset();
		modelGrid.reset();
		modelGrid.startEntities();
	}

	/**
	 * Stops the game and closes the window.
	 */
	public void handleStop(ActionEvent actionEvent){
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Returns the first difficulty selected in the second tab of the application. See the main.fxml file for more information.
	 */
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

	/**
	 * Returns the number of ghosts selected in the slider in the third menu. See the main.fxml file for more information.
	 */
	private int getGhosts(){
		ObservableList<MenuItem> it = diffs.get(2).getItems();
		Slider slider = (Slider)it.get(0).getGraphic();
		return (int)slider.getValue();
	}

}

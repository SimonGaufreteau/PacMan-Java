package TasksThreads;

import Controller.ThreadController;
import Modele.Grille;
import javafx.concurrent.Task;

public class EndOfGameTask extends Task<Integer> {

	private final static String VICTORY_TEXT="finished";
	private Grille modelGrid;
	private ThreadController controller;
	public EndOfGameTask(Grille grille, ThreadController threadController){
		controller=threadController;
		this.modelGrid=grille;
	}

	@Override
	protected Integer call(){
		while(modelGrid.getNbBonusLeft()!=0){
			if(isCancelled()) return 1;
		}
		controller.stopGame();
		updateMessage(VICTORY_TEXT);
		updateProgress(1,1);

		return 0;
	}
}

package TasksThreads;

import Controller.ThreadController;
import Modele.Grille;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;

public class EndOfGameTask extends Task<Integer> {
	private final static String VICTORY_TEXT="You've won !";
	private final static String LOSE_TEXT="You've lost !";
	private Grille modelGrid;
	private ThreadController controller;
	private AtomicBoolean running= new AtomicBoolean(false);

	public EndOfGameTask(Grille grille, ThreadController threadController){
		controller=threadController;
		this.modelGrid=grille;
	}

	@Override
	protected Integer call(){
		running.set(true);
		while(modelGrid.getNbBonusLeft()!=0 && modelGrid.getPacMan().hasLives() && modelGrid.hasGhosts()){
			if(isCancelled()) break;
		}
		controller.stopGame();
		if(modelGrid.getNbBonusLeft()==0){
			updateMessage(VICTORY_TEXT);
		}
		else{
			updateMessage(LOSE_TEXT);
		}
		updateProgress(1,1);
		return 0;
	}
}

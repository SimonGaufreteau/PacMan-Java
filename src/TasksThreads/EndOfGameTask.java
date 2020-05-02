package TasksThreads;

import Controller.ThreadController;
import Modele.Grille;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;

public class EndOfGameTask extends Task<Integer> {
	private final static String VICTORY_TEXT="You've won !";
	private final static String LOSE_TEXT="You've lost !";
	private Grille modelGrid;
	private AtomicBoolean running;
	private ThreadController controller;

	public EndOfGameTask(Grille grille, ThreadController threadController){
		reset(grille, threadController);
	}

	public void reset(Grille grille,ThreadController threadController) {
		controller = threadController;
		this.modelGrid = grille;
		running = new AtomicBoolean(false);
	}

	@Override
	protected Integer call(){
		running.set(true);
		System.out.println("test");
		updateMessage("Waiting for the results...");
		while(modelGrid.getNbBonusLeft()!=0 && modelGrid.getPacMan().hasLives() && modelGrid.hasGhosts()){
			if(!running.get()) break;
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

	public void stopRunning(){
		running.set(false);
	}
}

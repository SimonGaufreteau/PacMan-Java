package TasksThreads;

import Controller.ThreadController;
import Modele.ModelGrid;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A task used to detect the win/lose conditions. Also used to display the remaining bonuses/ghosts left and the time played for each game.
 */
public class EndOfGameTask extends Task<Integer> {
	private final static String VICTORY_TEXT="You've won";
	private final static String LOSE_TEXT="You've lost !";
	private ModelGrid modelGrid;
	private AtomicBoolean running;
	private ThreadController controller;

	public EndOfGameTask(ModelGrid modelGrid, ThreadController threadController){
		reset(modelGrid, threadController);
	}

	public void reset(ModelGrid modelGrid, ThreadController threadController) {
		controller = threadController;
		this.modelGrid = modelGrid;
		running = new AtomicBoolean(false);
	}

	@Override
	protected Integer call(){
		running.set(true);
		updateMessage("Waiting for the results...");
		long time = System.currentTimeMillis();
		while(modelGrid.getNbBonusLeft()!=0 && modelGrid.getPacMan().hasLives() && modelGrid.hasGhosts()){
			updateMessage("Eat "+modelGrid.getNbBonusLeft()+" points or kill the "+modelGrid.getNumberOfGhosts()+" remaining ghosts to win ! (Time : "+(System.currentTimeMillis()-time)/1000+"s)");
			if(!running.get()) break;
		}
		controller.stopGame();
		time = (System.currentTimeMillis()-time)/1000;
		if(modelGrid.getNbBonusLeft()==0 || modelGrid.getNumberOfGhosts()==0){
			updateMessage(VICTORY_TEXT+" in "+time+" seconds !");
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

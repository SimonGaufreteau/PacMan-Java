package TasksThreads;

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

	public EndOfGameTask(ModelGrid modelGrid){
		reset(modelGrid);
	}

	public void reset(ModelGrid modelGrid) {
		this.modelGrid = modelGrid;
		running = new AtomicBoolean(false);
	}

	@Override
	protected Integer call(){
		running.set(true);
		long lastFinishedTime=0;
		while (running.get()){
			long time = System.currentTimeMillis();
			while(modelGrid.isFinished()){
				lastFinishedTime = (System.currentTimeMillis() - time) / 1000;
				updateMessage("Eat "+modelGrid.getNbBonusLeft()+" points or kill the "+modelGrid.getNumberOfGhosts()+" remaining ghosts to win ! (Time : "+lastFinishedTime+"s)");
				if(!running.get()) break;
			}
			modelGrid.stopEntities();
			if(modelGrid.getNbBonusLeft()==0 || modelGrid.getNumberOfGhosts()==0){
				updateMessage(VICTORY_TEXT+" in "+lastFinishedTime+" seconds !");
			}
			else{
				updateMessage(LOSE_TEXT);
			}
		}
		updateProgress(1,1);
		return 0;
	}

	public void stopRunning(){
		running.set(false);
	}

	public void startRunning(){
		running.set(true);
	}
}

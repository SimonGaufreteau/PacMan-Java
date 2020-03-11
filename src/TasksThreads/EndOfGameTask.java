package TasksThreads;

import Modele.Grille;
import javafx.concurrent.Task;

public class EndOfGameTask extends Task<Integer> {

	private final static String VICTORY_TEXT="finished";
	private Grille modelGrid;
	public EndOfGameTask(Grille grille){
		this.modelGrid=grille;
	}

	@Override
	protected Integer call(){
		while(modelGrid.getNbBonusLeft()!=0){
			if(isCancelled()) return 1;
		}
		modelGrid.interrupt();
		updateMessage(VICTORY_TEXT);
		updateProgress(1,1);

		return 0;
	}
}

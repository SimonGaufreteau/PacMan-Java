package Tasks;

import Modele.Grille;
import javafx.concurrent.Task;

public class EndOfGameTask extends Task<Integer> {

	private final static String ENDTEXT="finished";
	private Grille grille;
	public EndOfGameTask(Grille grille){
		this.grille=grille;
	}

	@Override
	protected Integer call() throws Exception {
		while(grille.getNbBonusLeft()!=0){
			if(isCancelled()) return 1;
		}
		updateProgress(1,1);
		return 0;
	}

	@Override
	protected void succeeded() {
		super.succeeded();
		updateMessage(ENDTEXT);
		grille.interrupt();
	}
}

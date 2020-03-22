package TasksThreads;

import Modele.SimplePacMan;
import javafx.concurrent.Task;

public class LivesTask extends Task<String> {
	SimplePacMan simplePacMan;
	public LivesTask(SimplePacMan spm){
		this.simplePacMan=spm;
	}
	@Override
	protected String call() throws Exception {
		while(simplePacMan.hasLives()){
			updateMessage(String.valueOf(simplePacMan.getLives()));
		}
		return null;
	}
}

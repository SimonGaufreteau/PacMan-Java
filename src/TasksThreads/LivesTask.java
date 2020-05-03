package TasksThreads;

import Modele.SimplePacMan;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A task used to display the remaining lives left in a String.
 * @deprecated Replaced by the {@link DisplayThread} which displays images instead of text.
 * Because this class can still be useful for terminal-display applications, it will remain in the project.
 *
 */
@Deprecated
public class LivesTask extends Task<String> {
	SimplePacMan simplePacMan;
	private AtomicBoolean running=new AtomicBoolean(false);
	public LivesTask(SimplePacMan spm){
		this.simplePacMan=spm;
	}
	@Override
	protected String call() {
		running.set(true);
		while(running.get()){
			updateMessage(simplePacMan.getLives()+" lives left !");
		}
		return null;
	}

	public void setPacman(SimplePacMan pacMan) {
		this.simplePacMan=pacMan;
	}

	public void stopRunning(){
		running.set(false);
	}
}

package TasksThreads;

import Modele.SimplePacMan;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;

public class LivesTask extends Task<String> {
	SimplePacMan simplePacMan;
	private AtomicBoolean running=new AtomicBoolean(false);
	public LivesTask(SimplePacMan spm){
		this.simplePacMan=spm;
	}
	@Override
	protected String call() {
		running.set(true);
		System.out.println("Called the lives task");
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

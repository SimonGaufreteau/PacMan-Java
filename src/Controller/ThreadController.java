package Controller;

import Modele.Entite;
import Modele.Grille;
import Tasks.EndOfGameTask;

import java.util.ArrayList;

public class ThreadController {
	private EndOfGameTask endOfGameTask;
	private ArrayList<Thread> threads;
	private ArrayList<Entite> entites;

	public ThreadController(Grille modelGrid){
		resetEndOfGameTask(modelGrid);
	}

	public void startThreads(){
		//Starting the end condition for the game.
		for(Entite e:entites){
			e.start();
		}
		for(Thread thread:threads){
			thread.start();
		}

	}

	public void interruptThreads(){
		for(Thread thread:threads){
			thread.interrupt();
		}
		for(Entite e:entites){
			e.interrupt();
		}
	}

	public void resetEndOfGameTask(Grille modelGrid)  {
		threads=new ArrayList<>();
		endOfGameTask = new EndOfGameTask(modelGrid);
		threads.add(new Thread(endOfGameTask));


		entites=new ArrayList<>();
		entites.addAll(modelGrid.getMap().keySet());
	}

	public EndOfGameTask getEndOfGameTask() {
		return endOfGameTask;
	}


}

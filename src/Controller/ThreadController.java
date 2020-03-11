package Controller;

import Modele.Entite;
import Modele.Grille;
import TasksThreads.DisplayThread;
import TasksThreads.EndOfGameTask;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ThreadController {
	private EndOfGameTask endOfGameTask;
	private ArrayList<Thread> threads;
	private ArrayList<Entite> entites;

	public ThreadController(Grille modelGrid, ImageView[][] tab){
		resetEndOfGameTask(tab,modelGrid);
	}

	public void startThreads(){
		//Starting the end condition for the game.
		for(Thread thread:threads){
			thread.start();
		}
		for(Entite e:entites){
			e.start();
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

	public void resetEndOfGameTask(ImageView[][] tab,Grille modelGrid)  {
		threads=new ArrayList<>();
		endOfGameTask = new EndOfGameTask(modelGrid);
		threads.add(new Thread(endOfGameTask));
		DisplayThread displayThread=new DisplayThread(tab,modelGrid);
		threads.add(displayThread);

		entites=new ArrayList<>();
		entites.addAll(modelGrid.getMap().keySet());
	}

	public EndOfGameTask getEndOfGameTask() {
		return endOfGameTask;
	}


}

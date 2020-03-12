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
	private Grille modelGrid;

	public ThreadController(Grille modelGrid, ImageView[][] tab){
		resetThreads(tab,modelGrid);
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

	public void resetThreads(ImageView[][] tab,Grille modelGrid)  {
		this.modelGrid=modelGrid;
		threads=new ArrayList<>();
		endOfGameTask = new EndOfGameTask(modelGrid,this);
		threads.add(new Thread(endOfGameTask));
		DisplayThread displayThread=new DisplayThread(tab,modelGrid);
		threads.add(new Thread(displayThread));
		entites=new ArrayList<>();
		entites.addAll(modelGrid.getMap().keySet());
	}

	public void stopGame(){
		modelGrid.interrupt();
	}

	public EndOfGameTask getEndOfGameTask() {
		return endOfGameTask;
	}

	public void resetGrid(){
		try {
			modelGrid.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

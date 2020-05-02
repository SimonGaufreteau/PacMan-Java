package Controller;

import Modele.Entite;
import Modele.Grille;
import TasksThreads.DisplayThread;
import TasksThreads.EndOfGameTask;
import TasksThreads.LivesTask;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ThreadController {
	private EndOfGameTask endOfGameTask;
	private DisplayThread displayThread;
	private LivesTask livesTask;
	private ArrayList<Thread> threads;
	private ArrayList<Entite> entites;
	private Grille modelGrid;

	public ThreadController(Grille modelGrid, ImageView[][] tab){
		resetThreads(tab,modelGrid);
	}

	public void startThreads(){
		for(Thread thread:threads){
			thread.start();
		}
		for(Entite e:entites){
			e.start();
		}
	}

	public void interruptThreads(){
		endOfGameTask.cancel();
		displayThread.stopThread();
		for(Thread thread:threads){
			thread.interrupt();
		}
		for(Entite e:entites){
			e.stopEntite();
		}
	}

	public void resetThreads(ImageView[][] tab,Grille modelGrid)  {
		this.modelGrid=modelGrid;
		threads=new ArrayList<>();
		endOfGameTask = new EndOfGameTask(modelGrid,this);
		threads.add(new Thread(endOfGameTask));
		displayThread=new DisplayThread(tab,modelGrid);
		threads.add(new Thread(displayThread));
		if(livesTask==null) {
			livesTask= new LivesTask(modelGrid.getPacMan());
		}
		else livesTask.setPacman(modelGrid.getPacMan());
		threads.add(new Thread(livesTask));
		entites=new ArrayList<>();
		entites.addAll(modelGrid.getMap().keySet());
	}

	public void stopGame(){
		modelGrid.interrupt();
	}

	public EndOfGameTask getEndOfGameTask() {
		return endOfGameTask;
	}
	public LivesTask getLivesTask(){return livesTask;}

	public void resetGrid(){
		try {
			modelGrid.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopAll(){
		stopGame();
		interruptThreads();
		livesTask.stopRunning();
	}
}

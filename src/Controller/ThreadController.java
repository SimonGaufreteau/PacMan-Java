package Controller;

import Modele.Entite;
import Modele.Grille;
import TasksThreads.DisplayThread;
import TasksThreads.EndOfGameTask;
import TasksThreads.LivesTask;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ThreadController {
	private EndOfGameTask endOfGameTask;
	private DisplayThread displayThread;
	private LivesTask livesTask;
	private ArrayList<Thread> threads;
	private ArrayList<Entite> entites;
	private Grille modelGrid;
	private Text text;
	private BorderPane borderPane;
	private BindingController.direction paneDirection;

	public ThreadController(Grille modelGrid, ImageView[][] tab, ImageView[] lifetab){
		resetThreads(tab,modelGrid,lifetab);
	}

	public void startThreads(){
		resetTextBinding();

		for(Thread thread:threads){
			thread.start();
		}
		for(Entite e:entites){
			e.start();
		}
	}

	public void interruptThreads(){
		endOfGameTask.stopRunning();
		displayThread.stopThread();
		for(Thread thread:threads){
			thread.interrupt();
		}
		for(Entite e:entites){
			e.stopEntite();
		}
	}

	public void resetThreads(ImageView[][] tab, Grille modelGrid, ImageView[] lifetab)  {
		this.modelGrid=modelGrid;
		threads=new ArrayList<>();

		displayThread=new DisplayThread(tab,modelGrid,lifetab);
		threads.add(new Thread(displayThread));
		if(livesTask==null) {
			livesTask= new LivesTask(modelGrid.getPacMan());
		}
		else livesTask.setPacman(modelGrid.getPacMan());
		threads.add(new Thread(livesTask));
		endOfGameTask = new EndOfGameTask(modelGrid,this);
		threads.add(new Thread(endOfGameTask));


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
		endOfGameTask.stopRunning();
	}

	public void setEndText(Text text, BorderPane borderPane,BindingController.direction direction){
		this.text = text;
		this.borderPane = borderPane;
		this.paneDirection = direction;
	}

	private void resetTextBinding(){
		BindingController.bindText(text,endOfGameTask,borderPane, paneDirection);
	}
}

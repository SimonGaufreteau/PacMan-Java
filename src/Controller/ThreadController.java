package Controller;

import Modele.Entity;
import Modele.ModelGrid;
import TasksThreads.DisplayThread;
import TasksThreads.EndOfGameTask;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * The main controller of the game. Starts the thread used in the game like {@link Modele.Entity}, {@link DisplayThread} and all the tasks in this module.
 */
public class ThreadController {
	private EndOfGameTask endOfGameTask;
	private DisplayThread displayThread;
	private ArrayList<Thread> threads;
	private ArrayList<Entity> entities;
	private ModelGrid modelGrid;
	private Text text;
	private BorderPane borderPane;
	private BindingController.direction paneDirection;

	public ThreadController(ModelGrid modelGrid, ImageView[][] tab, ImageView[] lifetab){
		resetThreads(tab,modelGrid,lifetab);
	}

	/**
	 * Starts the thread. This method should be called after the {@link #resetThreads(ImageView[][], ModelGrid, ImageView[])} method.
	 */
	public void startThreads(){
		resetTextBinding();

		for(Thread thread:threads){
			thread.start();
		}
		for(Entity e: entities){
			e.start();
		}
	}

	/**
	 * Interrupts all the running threads.
	 */
	public void interruptThreads(){
		endOfGameTask.stopRunning();
		displayThread.stopThread();
		for(Thread thread:threads){
			thread.interrupt();
		}
		for(Entity e: entities){
			e.stopEntity();
		}
	}

	/**
	 * Resets all the thread used in the game.
	 */
	public void resetThreads(ImageView[][] tab, ModelGrid modelGrid, ImageView[] lifetab)  {
		this.modelGrid=modelGrid;
		threads=new ArrayList<>();

		displayThread=new DisplayThread(tab,modelGrid,lifetab);
		threads.add(new Thread(displayThread));

		endOfGameTask = new EndOfGameTask(modelGrid,this);
		threads.add(new Thread(endOfGameTask));


		entities =new ArrayList<>();
		entities.addAll(modelGrid.getMap().keySet());
	}

	public void stopGame(){
		modelGrid.interrupt();
	}


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
		endOfGameTask.stopRunning();
	}

	public void setEndText(Text text, BorderPane borderPane,BindingController.direction direction){
		this.text = text;
		this.borderPane = borderPane;
		this.paneDirection = direction;
	}

	/**
	 * Used to reset the binding between the endTask and the text used to display it.
	 */
	private void resetTextBinding(){
		BindingController.bindText(text,endOfGameTask,borderPane, paneDirection);
	}
}

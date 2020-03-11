package Vue;

import Controller.EventController;
import Controller.ObserverController;
import Controller.ThreadController;
import Modele.Depl;
import Modele.Grille;
import Modele.SimplePacMan;
import Tasks.EndOfGameTask;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainVue extends Application {
	public final int BLOCK_SIZE=19; //number of pixels for an image
	public final String FILENAME="Stages/Stage1.txt";

	@Override
	public void start(Stage stage) throws Exception {
		//Init modelGrid (read-only here --> MVC)
		Grille modelGrid=new Grille(FILENAME);

		int SIZE_X=modelGrid.getSIZE_X();
		int SIZE_Y=modelGrid.getSIZE_Y();

		//Initialising an ImageView tab which will contains grid images.
		ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y];

		//Initialising the GridPane containing the actual images
		GridPane grid = new GridPane();


		//Init with empty images
		for (int i = 0; i < SIZE_X; i++) {
			for (int j = 0; j < SIZE_Y; j++) {
				ImageView img = new ImageView();
				tab[i][j] = img;
				grid.add(img, i, j);
			}
		}

		//Root node for javafx
		StackPane root = new StackPane();

		//Adding elements to the root
		root.getChildren().add(grid);
		Scene scene = new Scene(root, SIZE_X*BLOCK_SIZE, SIZE_Y*BLOCK_SIZE);

		//Setting the scene
		stage.setTitle("Pacman !");
		stage.setScene(scene);

		//Setting and starting the threads
		ObserverController.init(tab,modelGrid);
		ThreadController threadController = new ThreadController(modelGrid);
		threadController.startThreads();

		//Generating event handler
		EventController eventController = new EventController(modelGrid);
		root.setOnKeyPressed(event -> {
			try {
				/*
				* Note : this is a bit useless for now because nothing else than setDirection is done in the class
				* but to keep the MVC pattern clean and for future updates, we keep this design (for now).
				*/
				switch (event.getCode()){
					case LEFT:
						eventController.moveLeft();
						break;
					case RIGHT:
						eventController.moveRight();
						break;
					case UP:
						eventController.moveUp();
						break;
					case DOWN:
						eventController.moveDown();
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		/*Text text = new Text();
		text.setX(300);
		text.setY(300);
		text.textProperty().bind(threadController.getEndOfGameTask().messageProperty());
		root.getChildren().add(text);*/

		/*Button button = new Button("Retry ?");
		button.setOnAction(actionEvent -> {
			try {
				modelGrid.reset();
				threadController.resetEndOfGameTask(modelGrid);
				ObserverController.reset(tab,modelGrid);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		});
		root.getChildren().add(button);*/


		stage.show();
		grid.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
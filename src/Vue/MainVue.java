package Vue;

import Controller.EventController;
import Controller.ThreadController;
import Modele.Grille;
import TasksThreads.LivesTask;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainVue extends Application {
	public final int BLOCK_SIZE=19; //number of pixels for an image
	public final String FILENAME="Stages/Stage1.txt";

	@Override
	public void start(Stage stage) throws Exception {

		//Root node for javafx
		/*BorderPane root = new BorderPane();*/
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("main.fxml"));

		//Init modelGrid (read-only here --> MVC)
		Grille modelGrid=new Grille(FILENAME);
		int SIZE_X=modelGrid.getSIZE_X();
		int SIZE_Y=modelGrid.getSIZE_Y();

		//Initialising an ImageView tab which will contains grid images.
		ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y];

		ThreadController threadController = new ThreadController(modelGrid,tab);

		//Generating event handler
		EventController eventController = new EventController(modelGrid,threadController,tab);
		fxmlloader.setController(eventController);

		//Setting the binds
		BorderPane root = fxmlloader.load();
		eventController.setBindings(root);

		//binding the bottom text to the number of lives left
		Text text= (Text) root.getBottom();
		LivesTask livesTask = new LivesTask(modelGrid.getPacMan());
		text.textProperty().bind(livesTask.messageProperty());
		new Thread(livesTask).start();
		root.setBottom(text);
		stage.setResizable(true);

		//Initialising the GridPane containing the actual images
		GridPane grid = (GridPane) root.getCenter();

		//Init with empty images
		for (int i = 0; i < SIZE_X; i++) {
			for (int j = 0; j < SIZE_Y; j++) {
				ImageView img = new ImageView();
				tab[i][j] = img;
				tab[i][j].setFitHeight(BLOCK_SIZE);
				tab[i][j].setFitWidth(BLOCK_SIZE);
				grid.add(img, i, j);
			}
		}



		//Adding elements to the root
		//root.setCenter(grid);
		Scene scene = new Scene(root, SIZE_X*BLOCK_SIZE, SIZE_Y*BLOCK_SIZE);

		//Setting the scene
		stage.setTitle("Pacman !");
		stage.setScene(scene);

		//Setting and starting the threads
		threadController.startThreads();

		//Showing the stage with the grid on the focus
		stage.show();
		grid.requestFocus();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
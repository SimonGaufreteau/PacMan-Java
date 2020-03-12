package Vue;

import Controller.EventController;
import Controller.ThreadController;
import Modele.Grille;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
		ThreadController threadController = new ThreadController(modelGrid,tab);
		threadController.startThreads();

		//Generating event handler
		EventController eventController = new EventController(modelGrid);
		eventController.setBindings(root);


		//Adding a retry button
		Button button = new Button("Retry ?");
		button.setOnAction(actionEvent -> {
			try {
				threadController.interruptThreads();
				grid.requestFocus();

				threadController.resetGrid();

				eventController.reset(modelGrid);

				threadController.resetThreads(tab,modelGrid);
				threadController.startThreads();

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		});
		root.getChildren().add(button);


		stage.show();
		grid.requestFocus();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
package Vue;

import Controller.BindingController;
import Controller.EventController;
import Controller.ThreadController;
import Modele.ModelGrid;
import Modele.SimplePacMan;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main and only vue of the Application. See main.fxml file to see how this vue is constructed.
 */
public class MainVue extends Application {
	public final int BLOCK_SIZE=19; //number of pixels for an image
	public final String FILENAME="Stages/Stage1.txt";

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("main.fxml"));

		//Init modelGrid (read-only here --> MVC)
		ModelGrid modelGrid=new ModelGrid(FILENAME);
		int SIZE_X=modelGrid.getSIZE_X();
		int SIZE_Y=modelGrid.getSIZE_Y();

		//Initialising an ImageView tab which will contains grid images.
		ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y];
		ImageView[] lifetab = new ImageView[SimplePacMan.MAX_HEALTH];

		ThreadController threadController = new ThreadController(modelGrid,tab,lifetab);


		//Generating event handler
		EventController eventController = new EventController(modelGrid,threadController,tab,lifetab);
		fxmlloader.setController(eventController);
		BorderPane root = fxmlloader.load();


		//Setting the menus event to a pre-built controller
		ObservableList<Menu> menus = ((MenuBar)(root.getTop())).getMenus();
		eventController.setMenus(menus);

		//Setting the binds
		eventController.setBindings(root);

		stage.setResizable(false);

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


		//Init the healthbar
		BorderPane bottomPane = (BorderPane)(root.getBottom());
		GridPane lifeGrid = (GridPane) (bottomPane).getCenter();
		for(int i=0;i<3;i++){
			ImageView imageView = new ImageView();
			lifetab[i]=imageView;
			lifetab[i].setFitHeight(BLOCK_SIZE);
			lifetab[i].setFitWidth(BLOCK_SIZE);
			lifeGrid.add(imageView,i,0);
		}

		//Setting the bottom textbar for later uses in ThreadController and BindingController
		Text text = (Text)bottomPane.getBottom();
		threadController.setEndText(text,bottomPane,BindingController.direction.BOTTOM);


		//Adding elements to the root
		Scene scene = new Scene(root, SIZE_X*BLOCK_SIZE, SIZE_Y*(BLOCK_SIZE+3));

		//Setting the scene
		stage.setTitle("Pacman !");
		stage.setScene(scene);

		//Setting and starting the threads
		threadController.startThreads();

		stage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});

		//Showing the stage with the grid on the focus
		stage.show();
		grid.requestFocus();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
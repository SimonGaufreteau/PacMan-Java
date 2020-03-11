package Controller;

import Exceptions.AlreadyInitException;
import Modele.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Point;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ObserverController {
	private static String status="stopped";

	//Use init if you want a safe init of the Observer, user reset if you want to reset the grid / tab;
	public static void init(ImageView[][] tab, Grille modelGrid) throws Exception {
		if(status.equals("started")){
			throw new AlreadyInitException();
		}
		reset(tab,modelGrid);
	}

	public static void reset(ImageView[][] tab, Grille modelGrid) throws Exception {
		if(modelGrid==null){
			throw new Exception("The model grid is empty.");
		}
		int SIZE_X = modelGrid.getSIZE_X();
		int SIZE_Y = modelGrid.getSIZE_Y();
		Image imPM = new Image("Pacman.png");
		Image imVide = new Image("Vide.png");
		Image imFond = new Image("fondNoir.png");
		Image imBrique = new Image("Brique.jpg");
		Image imFantome = new Image("Fantome.jpg");
		Image imPoint = new Image("point.png");
		Image imBigPoint = new Image("Big-point.png");

		Observer o =  new Observer() { // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
			@Override
			public void update(Observable o, Object arg) {
				if(o instanceof SimplePacMan){
					//Updating the background images
					for (int i = 0; i < SIZE_X; i++) {
						for (int j = 0; j < SIZE_Y; j++) {
							if(modelGrid.getObjStatic(i,j)== ObjStatic.MUR)
								tab[i][j].setImage(imBrique);
							else{
								tab[i][j].setImage(imFond);
								if(modelGrid.getObjDynam(i,j)== ObjDynam.POINT) tab[i][j].setImage(imPoint);
								else if(modelGrid.getObjDynam(i,j)==ObjDynam.BONUS) tab[i][j].setImage(imBigPoint);
							}
						}
					}
					//Drawing Entities
					Map<Entite, Point> map= modelGrid.getMap();
					for(Entite e : map.keySet()){
						Point point=map.get(e);
						if(e instanceof SimplePacMan)
							tab[point.x][point.y].setImage(imPM);
						else if (e instanceof Fantome)
							tab[point.x][point.y].setImage(imFantome);
					}
				}
			}
		};

		//Starting the pacman Thread
		SimplePacMan spm = modelGrid.getPacMan();
		spm.addObserver(o);

		status="started";
	}
}

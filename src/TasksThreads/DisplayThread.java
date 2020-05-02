package TasksThreads;

import Modele.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DisplayThread extends Task<Void> {
	private ImageView[][] tab;
	private ImageView[] lifeTab;
	private Grille modelGrid;
	private AtomicBoolean running = new AtomicBoolean(false);
	public static final int REFRESH_DELAY = 2;
	private boolean firstInstance = true;
	Image imPM = new Image("Pacman.png");
	Image imVide = new Image("Vide.png");
	Image imFond = new Image("fondNoir.png");
	Image imBrique = new Image("wall.png");
	Image imFantome = new Image("Fantome.jpg");
	Image imPoint = new Image("point.png");
	Image imBigPoint = new Image("Big-point.png");
	Image imHeart = new Image("pixel_heart_19.png");

	//Use init if you want a safe init of the Observer, user reset if you want to reset the grid / tab;
	public DisplayThread(ImageView[][] tab, Grille modelGrid,ImageView[] lifeTab) {
		if (modelGrid == null) throw new NullPointerException();
		this.tab = tab;
		this.lifeTab=lifeTab;
		this.modelGrid = modelGrid;
	}

	@Override
	public Void call() {
		running.set(true);
		int SIZE_X = modelGrid.getSIZE_X();
		int SIZE_Y = modelGrid.getSIZE_Y();


		while (running.get()) {
			Platform.runLater(() -> {
				//Updating the background images
				for (int i = 0; i < SIZE_X; i++) {
					for (int j = 0; j < SIZE_Y; j++) {
						if (modelGrid.getObjStatic(i, j) == ObjStatic.MUR)
							tab[i][j].setImage(imBrique);
						else {
							tab[i][j].setImage(imFond);
							if (modelGrid.getObjDynam(i, j) == ObjDynam.POINT) tab[i][j].setImage(imPoint);
							else if (modelGrid.getObjDynam(i, j) == ObjDynam.BONUS) tab[i][j].setImage(imBigPoint);
						}
					}

				}
				//Drawing Entities
				Map<Entite, Point> map = modelGrid.getMap();
				for (Entite e : map.keySet()) {
					Point point = map.get(e);
					if (e instanceof SimplePacMan)
						tab[point.x][point.y].setImage(imPM);
					else if (e instanceof Fantome)
						tab[point.x][point.y].setImage(imFantome);
				}

				//Drawing life
				int lives;
				if(modelGrid.getPacMan()==null) lives=0;
				else  lives = modelGrid.getPacMan().getLives();
				//System.out.println("Lives : "+lives);
				for(int i=0;i<SimplePacMan.MAX_HEALTH;i++) {
					if (i < lives)
						lifeTab[i].setImage(imHeart);
					else
						lifeTab[i].setImage(imVide);
				}
			});
			if (firstInstance) {
				firstInstance = false;
			}
			try {
				Thread.sleep(REFRESH_DELAY);
			} catch (InterruptedException e) {
				//System.out.println("Thread stopped.");
				return null;
			}
		}
		return null;
	}


	public void stopThread(){
		running.set(false);
	}

	public boolean isFirstInstance() {
		return firstInstance;
	}
}

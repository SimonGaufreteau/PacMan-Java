package TasksThreads;

import Modele.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple thread used to update the main grid of game and the health bar.
 */
public class DisplayThread extends Task<Void> {
	private ImageView[][] tab;
	private ImageView[] lifeTab;
	private ModelGrid modelGrid;
	private AtomicBoolean running = new AtomicBoolean(false);
	public static final int REFRESH_DELAY = 2;
	private boolean firstInstance = true;

	//Declaration of the images
	Image imPM = new Image("Pacman.png");
	Image imEmpty = new Image("Empty.png");
	Image imBG = new Image("fondNoir.png");
	Image imWall = new Image("wall.png");
	Image imGhost = new Image("Ghost.jpg");
	Image imPoint = new Image("point.png");
	Image imBigPoint = new Image("Big-point.png");
	Image imHeart = new Image("pixel_heart_19.png");
	Image imStrong = new Image("pacman-strong.png");
	Image imUntouchable = new Image("pacman-untouchable.png");

	public DisplayThread(ImageView[][] tab, ModelGrid modelGrid, ImageView[] lifeTab) {
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
						if (modelGrid.getObjStatic(i, j) == ObjStatic.WALL)
							tab[i][j].setImage(imWall);
						else {
							tab[i][j].setImage(imBG);
							if (modelGrid.getObjDynam(i, j) == ObjDynam.POINT) tab[i][j].setImage(imPoint);
							else if (modelGrid.getObjDynam(i, j) == ObjDynam.BONUS) tab[i][j].setImage(imBigPoint);
						}
					}

				}
				//Drawing Entities
				Map<Entity, Point> map = modelGrid.getMap();
				for (Entity e : map.keySet()) {
					Point point = map.get(e);
					if (e instanceof SimplePacMan){
						if(((SimplePacMan) e).isInvisible())
							tab[point.x][point.y].setImage(imStrong);
						else if (((SimplePacMan) e).isUntouchable()){
							tab[point.x][point.y].setImage(imUntouchable);
						}
						else
							tab[point.x][point.y].setImage(imPM);
					}
					else if (e instanceof Ghost)
						tab[point.x][point.y].setImage(imGhost);
				}

				//Drawing lives
				int lives;
				if(modelGrid.getPacMan()==null) lives=0;
				else  lives = modelGrid.getPacMan().getLives();
				for(int i=0;i<SimplePacMan.MAX_HEALTH;i++) {
					if (i < lives)
						lifeTab[i].setImage(imHeart);
					else
						lifeTab[i].setImage(imEmpty);
				}
			});
			if (firstInstance) {
				firstInstance = false;
			}
			try {
				Thread.sleep(REFRESH_DELAY);
			} catch (InterruptedException e) {
				return null;
			}
		}
		return null;
	}


	public void stopThread(){
		running.set(false);
	}

}

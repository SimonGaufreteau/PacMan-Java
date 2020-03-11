package TasksThreads;

import Exceptions.AlreadyInitException;
import Modele.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DisplayThread extends Thread{
	private  ImageView[][] tab;
	private  Grille modelGrid;
	private AtomicBoolean running=new AtomicBoolean(false);
	public static final int REFRESH_DELAY=50;
	private boolean firstInstance=true;
	//Use init if you want a safe init of the Observer, user reset if you want to reset the grid / tab;
	public DisplayThread(ImageView[][] tab, Grille modelGrid){
		if(modelGrid==null) throw new NullPointerException();
		this.tab=tab;
		this.modelGrid=modelGrid;
	}

	@Override
	public void run() {
		running.set(true);
		int SIZE_X = modelGrid.getSIZE_X();
		int SIZE_Y = modelGrid.getSIZE_Y();
		Image imPM = new Image("Pacman.png");
		Image imVide = new Image("Vide.png");
		Image imFond = new Image("fondNoir.png");
		Image imBrique = new Image("Brique.jpg");
		Image imFantome = new Image("Fantome.jpg");
		Image imPoint = new Image("point.png");
		Image imBigPoint = new Image("Big-point.png");


		while(running.get()){
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

			if(firstInstance){
				firstInstance=false;
			}
			try {
				Thread.sleep(REFRESH_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void interrupt(){
		running.set(false);
	}

	public boolean isFirstInstance() {
		return firstInstance;
	}
}

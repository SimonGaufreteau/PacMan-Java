
package VueControleur;

import Modele.*;
import Tasks.EndOfGameTask;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author frederic.armetta
 */
public class SimpleVC extends Application {

    public final int BLOCK_SIZE=19; //number of pixels for an image
    public final String FILENAME="Stages/StageTest.txt";

    Grille grille;

    @Override
    public void start(Stage primaryStage) throws Exception {
        grille=new Grille(FILENAME);
        GridPane grid = new GridPane(); // création de la grille


        Image imPM = new Image("Pacman.png"); // préparation des images
        Image imVide = new Image("Vide.png");
        Image imFond = new Image("fondNoir.png");
        Image imBrique = new Image("Brique.jpg");
        Image imFantome = new Image("Fantome.jpg");
        Image imPoint = new Image("point.png");
        Image imBigPoint = new Image("Big-point.png");

        int SIZE_X=grille.getSIZE_X();
        int SIZE_Y=grille.getSIZE_Y();
        ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y]; // tableau permettant de récupérer les cases graphiques lors du rafraichissement

        for (int i = 0; i < SIZE_X; i++) { // initialisation de la grille (sans image)
            for (int j = 0; j < SIZE_Y; j++) {
                ImageView img = new ImageView();
                tab[i][j] = img;
                grid.add(img, i, j);
            }

        }


        StackPane root = new StackPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, SIZE_X*BLOCK_SIZE, SIZE_Y*BLOCK_SIZE);

        primaryStage.setTitle("Pacman !");
        primaryStage.setScene(scene);

        Observer o =  new Observer() { // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
            @Override
            public void update(Observable o, Object arg) {
                if(o instanceof Entite){
                    for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique
                        for (int j = 0; j < SIZE_Y; j++) {
                            if(grille.getObjStatic(i,j)== ObjStatic.MUR)
                                tab[i][j].setImage(imBrique);
                            else{
                                tab[i][j].setImage(imFond);
                                if(grille.getObjDynam(i,j)==ObjDynam.POINT) tab[i][j].setImage(imPoint);
                                else if(grille.getObjDynam(i,j)==ObjDynam.BONUS) tab[i][j].setImage(imBigPoint);

                            }
                        }
                    }
                    Map<Entite,Point> map= grille.getMap();
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
        SimplePacMan spm = grille.getPacMan();

        spm.addObserver(o);
        spm.start(); // on démarre spm

        EndOfGameTask endOfGameTask=new EndOfGameTask(grille);
        new Thread(endOfGameTask).start();
        Text text = new Text();
        text.setX(300);
        text.setY(300);
        text.textProperty().bind(endOfGameTask.messageProperty());
        root.getChildren().add(text);

        ArrayList<Fantome> fantomes = grille.getFantomes();
        for(Fantome f : fantomes){
            f.start();
        }


        primaryStage.show();

        root.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() { // on écoute le clavier


            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (event.isShiftDown()) {
                    grille.setPointEntite(spm, 0, 0); // si on clique sur shift, on remet spm en haut à gauche
                }
                try {
                    switch (event.getCode()){
                        case LEFT:
                            spm.setDirection(Depl.GAUCHE);
                            break;
                        case RIGHT:
                            spm.setDirection(Depl.DROIT);
                            break;
                        case UP:
                            spm.setDirection(Depl.HAUT);
                            break;
                        case DOWN:
                            spm.setDirection(Depl.BAS);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        grid.requestFocus();
    }




    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

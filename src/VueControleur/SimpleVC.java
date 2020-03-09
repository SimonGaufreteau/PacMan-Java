/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControleur;

import Modele.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;

/**
 *
 * @author frederic.armetta
 */
public class SimpleVC extends Application {

    public final int SIZE_X =10;
    public final int SIZE_Y = 10;

    Grille grille;
    /*public void updateMap(){
        Iterator e = map.entrySet().iterator();
        while (e.hasNext()){
            e.next();
        }
    }*/

    @Override
    public void start(Stage primaryStage) {

        grille=new Grille(SIZE_X,SIZE_Y);
        GridPane grid = new GridPane(); // création de la grille

        // Pacman.svg.png
        //Image image = new Image(getClass().getResource("za.jpg").toExternalForm());

        Image imPM = new Image("Pacman.png"); // préparation des images
        Image imVide = new Image("Vide.png");
        Image imFond = new Image("fondNoir.png");
        Image imBrique = new Image("Brique.jpg");
        Image imFantome = new Image("Fantome.jpg");
        //img.setScaleY(0.01);
        //img.setScaleX(0.01);

        ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y]; // tableau permettant de récupérer les cases graphiques lors du rafraichissement

        for (int i = 0; i < SIZE_X; i++) { // initialisation de la grille (sans image)
            for (int j = 0; j < SIZE_Y; j++) {
                ImageView img = new ImageView();

                tab[i][j] = img;

                grid.add(img, i, j);
            }

        }

        Observer o =  new Observer() { // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
            @Override
            public void update(Observable o, Object arg) {
                Point p = grille.getPacManCoord();
                Random r = new Random();
                for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique
                    for (int j = 0; j < SIZE_Y; j++) {
                        if(grille.getObjStatic(i,j)== ObjStatic.MUR)
                            tab[i][j].setImage(imBrique);
                        else
                            tab[i][j].setImage(imFond);
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
        };


        SimplePacMan spm = grille.getPacMan();

        spm.addObserver(o);
        spm.start(); // on démarre spm

        ArrayList<Fantome> fantomes = grille.getFantomes();
        for(Fantome f : fantomes){
            f.start();
        }

        StackPane root = new StackPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Pacman !");
        primaryStage.setScene(scene);
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

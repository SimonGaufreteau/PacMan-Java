/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControleur;

import Modele.Entite;
import Modele.Fantome;
import Modele.SimplePacMan;
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

    public Map<Entite, Point> map;

    public void updateMap(){
        Iterator e = map.entrySet().iterator();
        while (e.hasNext()){
            e.next();
        }
    }

    @Override
    public void start(Stage primaryStage) {

        map = new HashMap<>();
        int n=4;
        Random r = new Random();
        for (int i=0;i<n;i++){
            map.put(new Fantome(),new Point(r.nextInt(SIZE_X),r.nextInt(SIZE_Y)));
        }



        SimplePacMan spm = new SimplePacMan(SIZE_X, SIZE_Y); // initialisation du modèle
        map.put(spm,new Point(spm.getX(),spm.getY()));
        GridPane grid = new GridPane(); // création de la grille

        // Pacman.svg.png
        //Image image = new Image(getClass().getResource("za.jpg").toExternalForm());

        Image imPM = new Image("Pacman.png"); // préparation des images
        Image imVide = new Image("Vide.png");

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
                for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique
                    for (int j = 0; j < SIZE_Y; j++) {

                        if (spm.getX() == i && spm.getY() == j) { // spm est à la position i, j => le dessiner
                            tab[i][j].setImage(imPM);

                        } else {

                            tab[i][j].setImage(imVide);
                        }

                    }
                }
            }
        };





        spm.addObserver(o);
        spm.start(); // on démarre spm

        StackPane root = new StackPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() { // on écoute le clavier


            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (event.isShiftDown()) {
                    spm.initXY(); // si on clique sur shift, on remet spm en haut à gauche
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

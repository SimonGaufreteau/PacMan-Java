package Modele;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grille {
    private ObjStatic[][] grilleObj;
    private Map<Entite, Point> map;
    private int SIZE_X;
    private int SIZE_Y;


    public Grille(int x,int y){
        SIZE_Y=y;
        SIZE_X=x;
        map = new HashMap<>();
        int n=4;
        Random r = new Random();
        for (int i=0;i<n;i++){
            map.put(new Fantome(),new Point(r.nextInt(SIZE_X),r.nextInt(SIZE_Y)));
        }

        grilleObj=new ObjStatic[SIZE_X][SIZE_Y];
        for(int i=0;i<SIZE_X;i++){
            for (int j=0;j<SIZE_Y;j++){
                if(i==0&&j==0) grilleObj[i][j]=ObjStatic.VIDE;
                if(r.nextInt(100)<80)
                    grilleObj[i][j]=ObjStatic.VIDE;
                else grilleObj[i][j]=ObjStatic.MUR;
            }
        }

        SimplePacMan spm = new SimplePacMan(this,100);
        map.put(spm,new Point(0,0));
    }


    public Point getPacManCoord(){
        for (Entite entite : map.keySet()) {
            if (entite instanceof SimplePacMan) return map.get(entite);
        }
        return null;
    }

    public SimplePacMan getPacMan(){
        for (Entite entite : map.keySet()) {
            if (entite instanceof SimplePacMan) return (SimplePacMan)entite;
        }
        return null;
    }

    public Map<Entite, Point> getMap() {
        return map;
    }

    public ObjStatic[][] getGrille() {
        return grilleObj;
    }

    public void depl(Depl d,Entite e) throws Exception {
        Point p = map.get(e);
        if(p==null) throw new Exception("Invalid entity.");
        switch (d){
            case HAUT:
                if (p.y == 0) {
                    p.y = SIZE_Y-1;
                } else {
                    p.y--;
                }
                break;
            case BAS:
                if(p.y+1==SIZE_Y) p.y=0;
                else p.y++;
                break;
            case DROIT:
                if(p.x+1==SIZE_X) p.x=0;
                else p.x++;
                break;
            case GAUCHE:
                if(p.x==0) p.x=SIZE_X-1;
                else p.x--;
                break;
        }
    }

    public boolean OkDepl(Depl depl,Entite e) throws Exception {
        Point p = map.get(e);
        if(p==null) throw new Exception("Invalid entity.");
        switch(depl){
            case HAUT:
                if(p.y==0) return !isVide(p.x,SIZE_Y-1);
                return isVide(p.x,p.y-1);
            case BAS:
                if(p.y==SIZE_Y-1) return !isVide(p.x,0);
                return isVide(p.x,p.y+1);
            case DROIT:
                if(p.x+1==SIZE_X) return isVide(0,p.y);
                return isVide(p.x+1,p.y);
            case GAUCHE:
                if(p.x==0) return isVide(SIZE_X-1,p.y);
                return isVide(p.x-1,p.y);
            default:
                throw new Exception("Invalid deplacement.");
        }
    }

    private boolean isVide(int x, int y){
        return getObjStatic(x,y) == ObjStatic.VIDE;
    }

    public void setPointEntite(Entite e,int x,int y){
        map.put(e,new Point(x,y));
    }

    public ObjStatic getObjStatic(int i, int j) {
        return grilleObj[i][j];
    }
}

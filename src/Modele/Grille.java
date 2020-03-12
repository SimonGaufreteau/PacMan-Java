package Modele;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grille {
    private ObjStatic[][] grilleObj;
    private ObjDynam[][] grilleDynam;
    private Map<Entite, Point> map;
    private int SIZE_X;
    private int SIZE_Y;
    private Random r = new Random();
    private final int PACMAN_DELAY=50;
    private final int FANTOME_DELAY=50;
    private final int FANTOME_NUMBER=6;

    private final int GHOST_XPOS=12;
    private final int GHOST_YPOS=12;
    private final int PCM_XPOS=1;
    private final int PCM_YPOS=1;

    private int nbBonusLeft;
    private String FILENAME;

    private final String FILE_REGEX=" \\| ";

    public Grille(String filename) throws Exception {
        reset(filename);
    }

    public void reset() throws Exception {
        interrupt();
        reset(FILENAME);
    }

    public void reset(String filename) throws Exception {
        interrupt();
        this.FILENAME=filename;
        FileReader fileReader = new FileReader(new File(filename));
        BufferedReader br = new BufferedReader(fileReader);

        String line = br.readLine();
        String[] splitted = line.split(FILE_REGEX);
        getSizeFromLine(splitted);

        grilleObj = new ObjStatic[SIZE_X][SIZE_Y];
        grilleDynam = new ObjDynam[SIZE_X][SIZE_Y];
        line=br.readLine();
        int i=0;
        while(line!=null && i<SIZE_Y){
            splitted=line.split(FILE_REGEX);
            if(splitted.length!=SIZE_X) throw new Exception("Incorrect file format line : "+i+1+". Expected "+SIZE_X+" but found "+line.length());
            for (int i1 = 0; i1 < splitted.length; i1++) {
                String s = splitted[i1];
                if (s.length() != 1) throw new Exception("Incorrect character at line "+i+1+" character n°"+i1+" :"+s);
                char c = s.charAt(0);
                grilleObj[i1][i] = ObjStatic.getObjFromChar(c);
                grilleDynam[i1][i] = ObjDynam.getObjFromChar(c);
                if(grilleDynam[i1][i]!=null)nbBonusLeft++;
            }
            line=br.readLine();
            i++;
        }

        map=new HashMap<>();
        addGhosts(GHOST_XPOS,GHOST_YPOS);
        SimplePacMan simplePacMan = new SimplePacMan(this,PACMAN_DELAY);
        map.put(simplePacMan,new Point(PCM_XPOS,PCM_YPOS));
    }

    private void getSizeFromLine(String[] splitted) throws Exception {
        if(splitted.length!=2) throw new Exception("Invalid first line in the file.");
        try{
            this.SIZE_X=Integer.parseInt(splitted[0]);
            this.SIZE_Y=Integer.parseInt(splitted[1]);
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            throw e;
        }
    }


    public Grille(int x,int y){
        SIZE_Y=y;
        SIZE_X=x;
        map = new HashMap<>();
        generateRandomObjStaticGrid();
        addGhosts();
    }


    private void generateRandomObjStaticGrid(){
        grilleObj=new ObjStatic[SIZE_X][SIZE_Y];
        grilleDynam= new ObjDynam[SIZE_X][SIZE_Y];
        for(int i=0;i<SIZE_X;i++){
            for (int j=0;j<SIZE_Y;j++){
                if(i==0&&j==0) grilleObj[i][j]=ObjStatic.VIDE;
                else if(r.nextInt(100)<80) {
                    grilleObj[i][j] = ObjStatic.VIDE;
                    if(r.nextInt(100)<70)
                        grilleDynam[i][j]=ObjDynam.POINT;
                }
                else grilleObj[i][j]=ObjStatic.MUR;
            }
        }
        SimplePacMan spm = new SimplePacMan(this,PACMAN_DELAY);
        map.put(spm,new Point(0,0));
    }

    private void addGhosts(){
        for (int i = 0; i< FANTOME_NUMBER; i++){
            addOneGhost();
        }
    }

    private void addGhosts(int x,int y){
        for(int i=0;i<FANTOME_NUMBER;i++){
            addOneGhost(x,y);
        }
    }

    private void addOneGhost(){
        addOneGhost(r.nextInt(SIZE_X),r.nextInt(SIZE_Y));
    }

    private void addOneGhost(int x,int y){
        map.put(new Fantome(this,FANTOME_DELAY),new Point(x,y));
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

    public ArrayList<Fantome> getFantomes(){
        ArrayList<Fantome> tab = new ArrayList<>();
        for(Entite entite : map.keySet()){
            if(entite instanceof Fantome) tab.add((Fantome) entite);
        }
        return tab;
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
        if((getObjDynam(p.x,p.y)==ObjDynam.BONUS || getObjDynam(p.x,p.y)==ObjDynam.POINT )&& e instanceof SimplePacMan) {
            grilleDynam[p.x][p.y]=null;
            nbBonusLeft--;
        }
    }


    public boolean OkDepl(Depl depl,Entite e) throws Exception {
        Point p = map.get(e);
        if(depl==null) return false;
        if(p==null) throw new Exception("Invalid entity.");
        switch(depl){
            case HAUT:
                if(p.y==0) return isVide(p.x,SIZE_Y-1);
                return isVide(p.x,p.y-1);
            case BAS:
                if(p.y==SIZE_Y-1) return isVide(p.x,0);
                return isVide(p.x,p.y+1);
            case DROIT:
                if(p.x+1==SIZE_X) return isVide(0,p.y);
                return isVide(p.x+1,p.y);
            case GAUCHE:
                if(p.x==0) return isVide(SIZE_X-1,p.y);
                return isVide(p.x-1,p.y);
        }
        return false;
    }

    public void startThreads(){
        for(Entite e:map.keySet()){
            e.start();
        }
    }

    public void interrupt(){
        if(map==null) return;
        for(Entite e:map.keySet()){
            e.interrupt();
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

    public ObjDynam getObjDynam(int i,int j){
        return grilleDynam[i][j];
    }

    public int getNbBonusLeft() {
        return nbBonusLeft;
    }

    public int getSIZE_X() {
        return SIZE_X;
    }

    public int getSIZE_Y() {
        return SIZE_Y;
    }

    public int getPACMAN_DELAY() {
        return PACMAN_DELAY;
    }

    public int getFANTOME_DELAY() {
        return FANTOME_DELAY;
    }
}

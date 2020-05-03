package Modele;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Grille {
    private ObjStatic[][] grilleObj;
    private ObjDynam[][] grilleDynam;
    private ConcurrentMap<Entite, Point> map;
    private int SIZE_X;
    private int SIZE_Y;
    private Random r = new Random();
    private final int BASE_PACMAN_DELAY=100;
    private final int BASE_FANTOME_DELAY=200;
    private int PACMAN_DELAY=BASE_PACMAN_DELAY;
    private int FANTOME_DELAY=BASE_FANTOME_DELAY;
    private int FANTOME_NUMBER=4;

    private int GHOST_XPOS;
    private int GHOST_YPOS;
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
        this.nbBonusLeft=0;
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

        GHOST_XPOS = SIZE_X/2;
        GHOST_YPOS = SIZE_Y/2;
        map=new ConcurrentHashMap<>();
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

    public SimplePacMan getPacMan(){
        for (Entite entite : map.keySet()) {
            if (entite instanceof SimplePacMan) return (SimplePacMan)entite;
        }
        return null;
    }

    public Map<Entite, Point> getMap() {
        return map;
    }

    public synchronized void depl(Depl d,Entite e) throws Exception {
        Point p = map.get(e);
        if(d==null) return;
        if(p==null) {
            //Note : this can happen when an Entity tries to move while she was already stopped below.
            throw new Exception("Invalid entity.");
        }
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
            if(getObjDynam(p.x,p.y)==ObjDynam.BONUS){
                //System.out.println("pacman set invincible !");
                ((SimplePacMan)e).setInvisible();
            }
            grilleDynam[p.x][p.y]=null;
            nbBonusLeft--;
        }

        //Checking if Pacman is losing a life and if a ghost is dying
        for(Entite entite:map.keySet()){
            if(map.get(entite)==null) break;
            Point point = map.get(entite).getLocation();
            if(point.x==p.x && point.y==p.y && entite.getClass()!=e.getClass()){
                if(e instanceof SimplePacMan && !((SimplePacMan) e).isUntouchable()){
                    if(((SimplePacMan) e).isInvisible()){
                        entite.lives--;
                    }
                    else{
                        ((SimplePacMan) e).setUntouchable();
                        e.lives--;
                    }
                }
                else if (entite instanceof SimplePacMan && !(((SimplePacMan) entite).isUntouchable())){
                    if(((SimplePacMan) entite).isInvisible()){
                        e.lives--;
                    }
                    else{
                        ((SimplePacMan) entite).setUntouchable();
                        entite.lives--;
                    }
                }
                if(e.lives==0){
                    e.stopEntite();
                    map.remove(e);
                }
                else if(entite.lives==0){
                    entite.stopEntite();
                    map.remove(entite);
                }
            }
        }
    }


    public boolean OkDepl(Depl depl,Entite e) throws Exception {
        Point p = map.get(e);
        if(depl==null) return false;
        if(p==null) {
            throw new Exception("Invalid entity.");
        }
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

    public void interrupt(){
        if(map==null) return;
        for(Entite e:map.keySet()){
            e.stopEntite();
        }
    }

    private boolean isVide(int x, int y){
        return getObjStatic(x,y) == ObjStatic.VIDE;
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

    public void setNbBonusLeft(int nbBonusLeft) {
        this.nbBonusLeft = nbBonusLeft;
    }

    public int getNumberOfGhosts(){
        int ret = 0;
        for(Entite e :map.keySet()){
            if (e instanceof Fantome) {
                ret++;
            }
        }
        return ret;
    }
    public boolean hasGhosts() {
        return getNumberOfGhosts()>0;
    }

    public void changeDifficulty(int diff){
        FANTOME_DELAY=BASE_FANTOME_DELAY-50*(diff-1);
    }

    public void setFANTOME_NUMBER(int fantome_number){
        FANTOME_NUMBER=fantome_number;
    }
}

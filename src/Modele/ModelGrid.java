package Modele;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Class used to model the game grid with static and dynamic objects.
 * @see Entity
 * @see ObjDynam
 * @see ObjStatic
 */
public class ModelGrid implements Runnable {
    private ObjStatic[][] gridObj;
    private ObjDynam[][] gridDynam;
    private ConcurrentMap<Entity, Point> map;
    private int SIZE_X;
    private int SIZE_Y;
    private Random r = new Random();
    private final int BASE_PACMAN_DELAY=100;
    private final int BASE_GHOST_DELAY =200;
    private int GHOST_DELAY = BASE_GHOST_DELAY;
    private int GHOST_NUMBER =4;

    private int GHOST_XPOS;
    private int GHOST_YPOS;
    private final int PCM_XPOS=1;
    private final int PCM_YPOS=1;

    private int nbBonusLeft;
    private String FILENAME;

    private SimplePacMan spm;
    private final String FILE_REGEX=" \\| ";

    public ModelGrid(String filename) throws Exception {
        reset(filename);
    }

    public void reset() throws Exception {
        stopEntities();
        reset(FILENAME);
    }

    /**
     * Resets the grid from a file. A correct file follows these rules : the separator between cells is a pipe (the '|' char).
     * The first line describes the size of the file as follows : numberOfColumns | numberOfLines.
     * The following lines correspond to a matrix of this size.
     * @apiNote This method also generates Entities, the Ghosts will always spawn in the middle of the grid and Pacman at x=y=1. This can be changed with the {@link #PCM_XPOS} and {@link #PCM_YPOS} variables.
     */
    public void reset(String filename) throws Exception {
        stopEntities();
        this.nbBonusLeft=0;
        this.FILENAME=filename;
        FileReader fileReader = new FileReader(new File(filename));
        BufferedReader br = new BufferedReader(fileReader);

        String line = br.readLine();
        String[] splitted = line.split(FILE_REGEX);
        getSizeFromLine(splitted);

        gridObj = new ObjStatic[SIZE_X][SIZE_Y];
        gridDynam = new ObjDynam[SIZE_X][SIZE_Y];
        line=br.readLine();
        int i=0;
        while(line!=null && i<SIZE_Y){
            splitted=line.split(FILE_REGEX);
            if(splitted.length!=SIZE_X) throw new Exception("Incorrect file format line : "+i+1+". Expected "+SIZE_X+" but found "+line.length());
            for (int i1 = 0; i1 < splitted.length; i1++) {
                String s = splitted[i1];
                if (s.length() != 1) throw new Exception("Incorrect character at line "+i+1+" character n°"+i1+" :"+s);
                char c = s.charAt(0);
                gridObj[i1][i] = ObjStatic.getObjFromChar(c);
                gridDynam[i1][i] = ObjDynam.getObjFromChar(c);
                if(gridDynam[i1][i]!=null)nbBonusLeft++;
            }
            line=br.readLine();
            i++;
        }

        GHOST_XPOS = SIZE_X/2;
        GHOST_YPOS = SIZE_Y/2;
        map=new ConcurrentHashMap<>();
        addGhosts(GHOST_XPOS,GHOST_YPOS);

        if(spm==null)
            spm = new SimplePacMan(this);
        else
            spm.reset();
        map.put(spm,new Point(PCM_XPOS,PCM_YPOS));
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

    /**
     * The following methods ({@link #addGhosts(int, int)}} and {@link #addOneGhost()} are used to add a new Ghost in the Entity map.
     */
    private void addGhosts(){
        for (int i = 0; i< GHOST_NUMBER; i++){
            addOneGhost();
        }
    }

    /**
     * @see #addGhosts()
     */
    private void addGhosts(int x,int y){
        for(int i = 0; i< GHOST_NUMBER; i++){
            addOneGhost(x,y);
        }
    }

    /**
     * @see #addGhosts()
     */
    private void addOneGhost(){
        addOneGhost(r.nextInt(SIZE_X),r.nextInt(SIZE_Y));
    }

    /**
     * @see #addGhosts()
     */
    private void addOneGhost(int x,int y){
        map.put(new Ghost(this, GHOST_DELAY),new Point(x,y));
    }

    public SimplePacMan getPacMan(){
        return spm;
    }

    public Map<Entity, Point> getMap() {
        return map;
    }

    /**
     * Moves the entity in the given direction. This method should always be called AFTER the {@link #OkDepl(Depl, Entity)} has been checked with the same parameters.
     * Note that this method is also checking if PacMan / a Ghost is losing a life.
     */

    public synchronized void depl(Depl d, Entity e) throws Exception {
        Point p = map.get(e);
        if(d==null) return;
        if(p==null) {
            //Note : this can happen when an Entity tries to move while she was already stopped below.
            throw new Exception("Invalid entity.");
        }
        switch (d){
            case UP:
                if (p.y == 0) {
                    p.y = SIZE_Y-1;
                } else {
                    p.y--;
                }
                break;
            case DOWN:
                if(p.y+1==SIZE_Y) p.y=0;
                else p.y++;
                break;
            case RIGHT:
                if(p.x+1==SIZE_X) p.x=0;
                else p.x++;
                break;
            case LEFT:
                if(p.x==0) p.x=SIZE_X-1;
                else p.x--;
                break;
        }
        if((getObjDynam(p.x,p.y)==ObjDynam.BONUS || getObjDynam(p.x,p.y)==ObjDynam.POINT )&& e instanceof SimplePacMan) {
            if(getObjDynam(p.x,p.y)==ObjDynam.BONUS){
                ((SimplePacMan)e).setInvisible();
            }
            gridDynam[p.x][p.y]=null;
            nbBonusLeft--;
        }

        //Checking if Pacman is losing a life and if a ghost is dying
        for(Entity entity :map.keySet()){
            if(map.get(entity)==null) break;
            Point point = map.get(entity).getLocation();
            if(point.x==p.x && point.y==p.y && entity.getClass()!=e.getClass()){
                if(e instanceof SimplePacMan && !((SimplePacMan) e).isUntouchable()){
                    if(((SimplePacMan) e).isInvisible()){
                        entity.lives--;
                    }
                    else{
                        ((SimplePacMan) e).setUntouchable();
                        e.lives--;
                    }
                }
                else if (entity instanceof SimplePacMan && !(((SimplePacMan) entity).isUntouchable())){
                    if(((SimplePacMan) entity).isInvisible()){
                        e.lives--;
                    }
                    else{
                        ((SimplePacMan) entity).setUntouchable();
                        entity.lives--;
                    }
                }
                if(e.lives==0){
                    e.stopEntity();
                    map.remove(e);
                }
                else if(entity.lives==0){
                    entity.stopEntity();
                    map.remove(entity);
                }
            }
        }
    }


    /**
     * Checks if the entity e can move in the direction given in argument
     */
    public boolean OkDepl(Depl depl, Entity e) throws Exception {
        Point p = map.get(e);
        if(depl==null) return false;
        if(p==null) {
            throw new Exception("Invalid entity.");
        }
        switch(depl){
            case UP:
                if(p.y==0) return isEmpty(p.x,SIZE_Y-1);
                return isEmpty(p.x,p.y-1);
            case DOWN:
                if(p.y==SIZE_Y-1) return isEmpty(p.x,0);
                return isEmpty(p.x,p.y+1);
            case RIGHT:
                if(p.x+1==SIZE_X) return isEmpty(0,p.y);
                return isEmpty(p.x+1,p.y);
            case LEFT:
                if(p.x==0) return isEmpty(SIZE_X-1,p.y);
                return isEmpty(p.x-1,p.y);
        }
        return false;
    }

    /**
     * Stops any entity still running in the grid.
     */
    public void stopEntities(){
        if(map==null) return;
        for(Entity e:map.keySet()){
            e.stopEntity();
        }
    }

    public void startEntities(){
        if(map==null) return;
        for(Entity e:map.keySet()){
            e.start();
        }
    }



    //GETTERS AND SETTERS
    private boolean isEmpty(int x, int y){
        return getObjStatic(x,y) == ObjStatic.EMPTY;
    }

    public ObjStatic getObjStatic(int i, int j) {
        return gridObj[i][j];
    }

    public ObjDynam getObjDynam(int i,int j){
        return gridDynam[i][j];
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

    public int getNumberOfGhosts(){
        int ret = 0;
        for(Entity e :map.keySet()){
            if (e instanceof Ghost) {
                ret++;
            }
        }
        return ret;
    }
    public boolean hasGhosts() {
        return getNumberOfGhosts()>0;
    }

    public void changeDifficulty(int diff){
        GHOST_DELAY = BASE_GHOST_DELAY -50*(diff-1);
    }

    public void setGHOST_NUMBER(int ghost_number){
        GHOST_NUMBER =ghost_number;
    }

    public boolean isRunning() {
        return (getNbBonusLeft()!=0 && getPacMan().hasLives() && hasGhosts());
    }

    @Override
    public void run() {
        startEntities();
        while (isRunning()){}
        stopEntities();
    }

}

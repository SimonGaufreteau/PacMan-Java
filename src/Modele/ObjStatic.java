package Modele;

/**
 * An enum used in the {@link ModelGrid} class to model a static object i.e. a wall or an empty cell.
 */
public enum ObjStatic {
    WALL, EMPTY;

    public static ObjStatic getObjFromChar(Character c) {
        if (c == 'W') {
            return WALL;
        }
        return EMPTY;
    }
}

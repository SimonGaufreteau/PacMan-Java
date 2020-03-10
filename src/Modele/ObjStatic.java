package Modele;

public enum ObjStatic {
    MUR,VIDE;

    public static ObjStatic getObjFromChar(Character c) {
        if (c == 'W') {
            return MUR;
        }
        return VIDE;
    }
}

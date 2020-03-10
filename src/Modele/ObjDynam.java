package Modele;

public enum ObjDynam {
	POINT, BONUS;

	public static ObjDynam getObjFromChar(Character c) {
		switch (c) {
			case 'P':
				return POINT;
			case 'B':
				return BONUS;
			default:
				return null;
		}
	}
}

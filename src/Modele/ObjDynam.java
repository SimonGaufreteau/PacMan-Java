package Modele;

/**
 * An enum used in the {@link ModelGrid} class to model a dynamic object i.e. a point or an bonus object which can be removed.
 */
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

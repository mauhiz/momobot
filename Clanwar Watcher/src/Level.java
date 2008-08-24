/**
 * 
 * @author abby
 *
 */
public enum Level {
	// {"noob", "low", "low+", "mid", "mid+", "good", "skilled", "high", "roxor"};
	UNKOWN(0),
	NOOB(1),
	LOW(2),
	LOWPLUS(3),
	MID(4),
	MIDPLUS(5),
	GOOD(6),
	SKILLED(7),
	ROXOR(8);
	
	/** L'attribut qui contient la valeur associé à l'enum */
	private final int code;
	
	/** Le constructeur qui associe une valeur à l'enum */
	private Level(int code) {
		this.code = code;
	}
	
	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public int getCode() {
		return this.code;
	}

}

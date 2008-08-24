/**
 * 
 * @author abby
 *
 */
public enum ServerStatus {
	UNKOWN(0),
	ON(1),
	OFF(2);
	
	/** L'attribut qui contient la valeur associé à l'enum */
	private final int code;
	
	/** Le constructeur qui associe une valeur à l'enum */
	private ServerStatus(int code) {
		this.code = code;
	}
	
	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public int getCode() {
		return this.code;
	}

}

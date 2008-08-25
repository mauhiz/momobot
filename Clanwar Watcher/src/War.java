import java.util.regex.*;

/**
 * 
 * @author abby
 *
 */
public class War {
	/**
	 * nbjoueur = 0 <=> UNKNOWN
	 */
	
	// Regexp magique de FenX
	//public static Pattern patternNbJoueurs = Pattern.compile("(\\d+)\\s?(vs|o|v)\\s?(\\d+)", Pattern.CASE_INSENSITIVE);
	public static Pattern patternNbJoueurs = Pattern.compile("(\\d+)\\s?(vs|o|c|x|n|on|/|v)\\s?(\\d+)");
	
	private int nbjoueurs;
	private Level level;
	private ServerStatus server;
	private String user;
	
	public int getNbjoueurs() {
		return nbjoueurs;
	}
	public void setNbjoueurs(int nbjoueurs) {
		this.nbjoueurs = nbjoueurs;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public ServerStatus getServer() {
		return server;
	}
	public void setServer(ServerStatus status) {
		this.server = status;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public War(){
		this.nbjoueurs = 0;
		this.level = Level.UNKOWN;
		this.server = ServerStatus.UNKOWN;
		this.user = "";
	}
	
	public War(String User, String SeekMessage){
		// Important
		SeekMessage = SeekMessage.toLowerCase();
		if(SeekMessage.contains("dispo") || SeekMessage.contains("tn") || SeekMessage.contains("last")){
			this.nbjoueurs = 0;
			this.level = Level.UNKOWN;
			this.server = ServerStatus.UNKOWN;
			this.user = "";
			return;
		}
		
		this.user = User;
			
		Matcher matcher = patternNbJoueurs.matcher(SeekMessage);
		
		/*
		 * On détecte le serveur
		 */
		if(SeekMessage.contains("on") || SeekMessage.contains("servok") || SeekMessage.contains("serv ok")){
			this.server = ServerStatus.ON;
		}
		else if (SeekMessage.contains("off")){
			this.server = ServerStatus.OFF;	
		}
		else{
			this.server = ServerStatus.UNKOWN;
		}
		
		/*
		 * On détecte le nombre de joueurs
		 */
		// TODO : a faire avec des regexp pour les différences nombres de joueurs.
		//  (\d+) ?(vs|o|v|on) ?(\d+)
		// case insensitive
		if(matcher.find()){
			if(matcher.group(1).compareTo(matcher.group(3)) == 0){
				this.nbjoueurs = Integer.parseInt(matcher.group(1));
			}
			/*int i;
			for(i=0; i<=matcher.groupCount(); i++){
				System.out.println("Groupe " + i + " : '" + matcher.group(i)+"'");
				
			}*/
		}
		else{
			// TODO : mettre le 55 en regexp (pour que ca marche aussi avec 33)
			if(SeekMessage.contains("pcw") || SeekMessage.contains("war") || SeekMessage.contains("pracc") || SeekMessage.contains("55"))
			{
				this.nbjoueurs = 5;
			}
			else{
				this.nbjoueurs = 0;
			}
		}
		
		/**
		 * On détecte le lvl
		 */
		if(SeekMessage.contains("roxor")){
			this.level = Level.ROXOR;
		}
		else if(SeekMessage.contains("skilled") || SeekMessage.contains("hard") || SeekMessage.contains("high")){
			this.level = Level.SKILLED;
		}
		else if(SeekMessage.contains("good") || SeekMessage.contains("goood")){
			this.level = Level.GOOD;
		}
		else if(SeekMessage.contains("mid+") || SeekMessage.contains("mid +")){
			this.level = Level.MIDPLUS;
		}
		else if(SeekMessage.contains("mid")){
			this.level = Level.MID;
		}
		else if(SeekMessage.contains("low+") || SeekMessage.contains("low +")){
			this.level = Level.LOWPLUS;
		}
		else if(SeekMessage.contains("low")){
			this.level = Level.LOW;
		}
		else if(SeekMessage.contains("noob")){
			this.level = Level.NOOB;
		}
		else {
			this.level = Level.UNKOWN;
		}
		
		
	}
	
	public boolean isNull(){
		int nulllevel = 0;
		if(this.level == Level.UNKOWN) nulllevel++;
		if(this.server == ServerStatus.UNKOWN) nulllevel++;
		if(this.nbjoueurs == 0) nulllevel++;
		if(nulllevel >= 2)
			return true;
		return false;
	}
	
}

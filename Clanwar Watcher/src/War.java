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
	// Inutile de mettre Pattern.CASE_INSENSITIVE
	// Parfait pour les 5vs5, 4x4, ...
	public static Pattern patternNbJoueurs = Pattern.compile("(\\d+)\\s?(vs|vv|o|c|x|n|on|/|v)\\s?(\\d+)");
	
	// Pour les joueurs en 55, 44, 33...
	//public static Pattern patternNbJoueursShort = Pattern.compile("(\\d)(\\d)");
	
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
		if(SeekMessage.contains("dispo") || 
				SeekMessage.contains("tn") ||
				SeekMessage.contains("last") ||
				SeekMessage.contains("binome") ||
				SeekMessage.contains("cherche team")){
			this.nbjoueurs = 0;
			this.level = Level.UNKOWN;
			this.server = ServerStatus.UNKOWN;
			this.user = "";
			return;
		}
		
		this.user = User;
			
		Matcher matcherNbJoueurs = patternNbJoueurs.matcher(SeekMessage);
		//Matcher matcherNbJoueursShort = patternNbJoueursShort.matcher(SeekMessage);
		/*
		 * On détecte le serveur
		 */
		// TODO : faire une regexp pour ca aussi ?
		// Du style : (serv)(2 caract maxi)(on|0n|ok|0k)
		// Espace avant le ' on' pour éviter de matcher le 5on5 off
		// Ca ne marche plus pour '5 on 5' OFF
		if(SeekMessage.contains(" on") ||
				SeekMessage.contains("servon") ||
				SeekMessage.contains("servok") ||
				SeekMessage.contains("serv ok") ||
				SeekMessage.contains("serv:ok") ||
				SeekMessage.contains("serv_on") ||
				SeekMessage.contains("serv0n") ||
				SeekMessage.contains("serv.on") ||
				SeekMessage.contains("have serv") ||
				SeekMessage.contains("gotserv")){
			this.server = ServerStatus.ON;
		}
		else if (SeekMessage.contains("off") ||
				SeekMessage.contains("0ff") ||
				SeekMessage.contains("noserv") ||
				SeekMessage.contains("no serv") ||
				SeekMessage.contains("pas de serv") ||
				SeekMessage.contains("pa de serv")){
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
		if(matcherNbJoueurs.find()){
			if(matcherNbJoueurs.group(1).compareTo(matcherNbJoueurs.group(3)) == 0){
				this.nbjoueurs = Integer.parseInt(matcherNbJoueurs.group(1));
			}
			/*int i;
			for(i=0; i<=matcherNbJoueurs.groupCount(); i++){
				System.out.println("Groupe " + i + " : '" + matcherNbJoueurs.group(i)+"'");
				
			}*/
		}
		/*else if(matcherNbJoueursShort.find()){
			
			// TODO: tester le code ci dessous
			if(matcherNbJoueurs.group(1).compareTo(matcherNbJoueurs.group(3)) == 0){
				this.nbjoueurs = Integer.parseInt(matcherNbJoueurs.group(1));
			}
			int i;
			for(i=0; i<=matcherNbJoueursShort.groupCount(); i++){
				System.out.println("Groupe " + i + " : '" + matcherNbJoueursShort.group(i)+"'");
				
			}
		}*/
		else{
			// TODO : mettre le 55 en regexp (pour que ca marche aussi avec 33)
			if(SeekMessage.contains("pcw") || 
					SeekMessage.contains("war") || 
					SeekMessage.contains("pracc") ||
					SeekMessage.contains("55") ||
					SeekMessage.contains("5 5"))
			{
				this.nbjoueurs = 5;
			}
			else if(SeekMessage.contains("44") ||
					SeekMessage.contains("4 4")){
				this.nbjoueurs = 4;
			}
			else if(SeekMessage.contains("33") ||
					SeekMessage.contains("3 3")){
				this.nbjoueurs = 3;
			}
			else if(SeekMessage.contains("22") ||
					SeekMessage.contains("2 2")){
				this.nbjoueurs = 2;
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
		else if(SeekMessage.contains("mid+") || 
				SeekMessage.contains("mid +") || 
				SeekMessage.contains("midl+") ||
				SeekMessage.contains("mi +")){
			this.level = Level.MIDPLUS;
		}
		else if(SeekMessage.contains("mid") || SeekMessage.contains("med")){
			this.level = Level.MID;
		}
		else if(SeekMessage.contains("low+") || SeekMessage.contains("low +")){
			this.level = Level.LOWPLUS;
		}
		else if(SeekMessage.contains("low")){
			this.level = Level.LOW;
		}
		else if(SeekMessage.contains("noob") || SeekMessage.contains("invincible")){
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
